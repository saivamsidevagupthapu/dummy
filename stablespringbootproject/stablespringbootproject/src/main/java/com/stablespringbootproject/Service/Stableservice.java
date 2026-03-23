package com.stablespringbootproject.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.stablespringbootproject.Dto.*;
import com.stablespringbootproject.Entity.*;
import com.stablespringbootproject.repository.*;

@Service
public class Stableservice {

    // -------------------------------------------------------
    // These are tools (dependencies) this service needs.
    // Spring will automatically provide them via constructor.
    // -------------------------------------------------------
    private final RestTemplate restTemplate;              // Used to call external APIs (like calling a URL)
    private final Countryrepo countryRepo;                // Talks to the Country table in database
    private final Countryservicerepo stableRepo;          // Talks to the CountryService table
    private final Vendorrepo vendorRepo;                  // Talks to the Vendor table
    private final Vendorapirepo vendorApiRepository;      // Talks to the VendorApi table
    private final VendorJsonMappingrepo jsonMappingRepo;  // Talks to the response mapping table
    private final Vehiclerequestmappingrepo vehicleRequestMappingRepo; // Talks to request mapping table

    // Constructor: Spring calls this automatically and fills in all the tools above
    public Stableservice(RestTemplate restTemplate, Countryrepo countryRepo,
                         Countryservicerepo stableRepo, Vendorrepo vendorRepo,
                         Vendorapirepo vendorApiRepository,
                         VendorJsonMappingrepo jsonMappingRepo,
                         Vehiclerequestmappingrepo vehicleRequestMappingRepo) {

        this.restTemplate = restTemplate;
        this.countryRepo = countryRepo;
        this.stableRepo = stableRepo;
        this.vendorRepo = vendorRepo;
        this.vendorApiRepository = vendorApiRepository;
        this.jsonMappingRepo = jsonMappingRepo;
        this.vehicleRequestMappingRepo = vehicleRequestMappingRepo;
    }

    
    public Stableresponse fetchVehicle(Stablerequest request) {

        // STEP 1: Find the country using the country code from the request
        // Example: request.getCountry() = "IN" → finds India in database
        Countryentity country = countryRepo.findByCountryCode(request.getCountry())
                .orElseThrow(() -> new RuntimeException("Country Not Found"));

        // STEP 2: Find an active service configured for this country
        Countryserviceentity service = stableRepo
                .findFirstByCountryCodeAndActiveTrue(country.getCountryCode())
                .orElseThrow(() -> new RuntimeException("No active service found"));

        // STEP 3: Find the vendor using vendor name + phone number from request
        Vendorentity vendor = vendorRepo.findByVendorNameIgnoreCaseAndPhoneNumber(
                request.getVendorname(), request.getPhone_number());

        // If no vendor found, stop and return a 404 error
        if (vendor == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor Not Found");
        }

        // STEP 4: Get the list of APIs this vendor supports for the requested type
        // Example: api_usage_type = "VEHICLE_LOOKUP" → get all APIs for that purpose
        List<Vendorapis> vendorApiList = vendorApiRepository
                .findByVendorIdAndApiType(vendor.getId(), request.getApi_usage_type());

        // Try each API one by one until one works
        for (Vendorapis vendorApi : vendorApiList) {

            // Get the request field mappings for this vendor + API combination
            // (These mappings tell us: which field goes in PATH, QUERY, HEADER, or BODY)
            List<vehiclerequestmapping> requestMappings =
                    vehicleRequestMappingRepo.findByVendorIdAndApiId(
                            vendor.getId(), vendorApi.getApiId());

            // Actually call the vendor's API and get the raw response
            Map<String, Object> rawVendorResponse =
                    callVendor(service, vendorApi, requestMappings, request);

            // If we got a valid response back
            if (rawVendorResponse != null) {

                // Get the response field mappings
                // (These tell us: which field in vendor's response maps to our internal field)
                List<Vehicleresponcemapping> responseMappings =
                        jsonMappingRepo.findByApiId(vendorApi.getApiId());

                // Convert the raw vendor response into our standard response format
                Stableresponse finalResponse =
                        mapVendorResponse(rawVendorResponse, responseMappings);

                // Also set the country code in our response
                finalResponse.setCountry(country.getCountryCode());

                return finalResponse; // Return the final answer
            }
        }

        // If none of the APIs returned any data, throw an error
        throw new RuntimeException("Vehicle not found");
    }

    // -------------------------------------------------------
    // This method actually calls the vendor's API (like visiting a URL)
    // It builds the URL, adds headers/params/body, and sends the request
    // -------------------------------------------------------
    private Map<String, Object> callVendor(Countryserviceentity service,
                                           Vendorapis vendorApi,
                                           List<vehiclerequestmapping> requestMappings,
                                           Stablerequest request) {

        // Build the full URL = base URL + API-specific path
        // Example: "https://vendor.com" + "/vehicle/{regNumber}" = "https://vendor.com/vehicle/{regNumber}"
        String fullUrl = service.getBaseUrl() + vendorApi.getApiUrl();

        // Convert the request object into a simple key-value map
        // Example: { "country": "IN", "vendorname": "ABC", "phone_number": "9999" }
        Map<String, String> requestFieldsMap = convertRequestToMap(request);

        // These maps will store values based on where they should be placed in the API call
        Map<String, String> pathVariables = new HashMap<>();   // Goes inside the URL path like /vehicle/{regNo}
        Map<String, String> queryParameters = new HashMap<>(); // Goes after ? like ?color=red
        Map<String, String> httpHeaders = new HashMap<>();     // Goes in request headers
        Map<String, Object> requestBody = new HashMap<>();     // Goes in request body (for POST)

        // Loop through each mapping rule and sort values into the correct place
        for (vehiclerequestmapping singleMapping : requestMappings) {

            // Use constant value if available, otherwise get the value from the request
            String value = singleMapping.getConstantValue() != null && !singleMapping.getConstantValue().isEmpty()
                    ? singleMapping.getConstantValue()                                        // Use fixed/constant value
                    : getIgnoreCase(requestFieldsMap, singleMapping.getStableField());        // Get from request

            // Skip if no value was found
            if (value == null) continue;

            // Put the value in the correct place based on its "location" type
            switch (singleMapping.getLocation()) {
                case PATH       -> pathVariables.put(singleMapping.getExternalName(), value);  // In URL path
                case QUERY      -> queryParameters.put(singleMapping.getExternalName(), value); // In URL query
                case HEADER     -> httpHeaders.put(singleMapping.getExternalName(), value);     // In HTTP header
                case BODY_JSON  -> requestBody.put(singleMapping.getExternalName(), value);     // In request body
            }
        }

        // Replace path variables in URL
        // Example: "/vehicle/{regNo}" + {regNo: "KA01"} → "/vehicle/KA01"
        fullUrl = resolveUrl(fullUrl, pathVariables);

        // If there are query parameters, append them to the URL
        // Example: "/vehicle/KA01" + {color: "red"} → "/vehicle/KA01?color=red"
        if (!queryParameters.isEmpty()) {
            UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(fullUrl);
            queryParameters.forEach(urlBuilder::queryParam);
            fullUrl = urlBuilder.toUriString();
        }

        // Add headers to the HTTP request
        HttpHeaders headers = new HttpHeaders();
        httpHeaders.forEach(headers::add);

        // Decide HTTP method: GET, POST, etc. from the database config
        HttpMethod httpMethod = HttpMethod.valueOf(vendorApi.getHttpMethod().toUpperCase());

        // For GET requests → no body needed; for POST/PUT → attach the body
        HttpEntity<?> httpRequest = httpMethod == HttpMethod.GET
                ? new HttpEntity<>(headers)                       // GET: only headers
                : new HttpEntity<>(requestBody, headers);         // POST/PUT: body + headers

        // Actually send the HTTP request and get the response
        ResponseEntity<Map> apiResponse =
                restTemplate.exchange(URI.create(fullUrl), httpMethod, httpRequest, Map.class);

        // Return the body of the response (the actual data)
        return apiResponse.getBody();
    }

    // -------------------------------------------------------
    // This method replaces {placeholders} in a URL with actual values
    // Example: "/vehicle/{regNo}" → "/vehicle/KA01AB1234"
    // -------------------------------------------------------
    private String resolveUrl(String urlWithPlaceholders, Map<String, String> pathVariables) {

        // This pattern finds anything inside curly braces like {regNo}, {state}
        Pattern placeholderPattern = Pattern.compile("\\{([^}]+)\\}");
        Matcher matcher = placeholderPattern.matcher(urlWithPlaceholders);

        StringBuffer finalUrl = new StringBuffer();

        // Keep finding the next {placeholder} and replace it
        while (matcher.find()) {
            String placeholderName = matcher.group(1); // e.g., "regNo"
            String actualValue = getIgnoreCase(pathVariables, placeholderName); // e.g., "KA01AB1234"

            // URL-encode the value so special characters are safe in a URL
            // Example: "Hello World" → "Hello%20World"
            matcher.appendReplacement(finalUrl,
                    URLEncoder.encode(actualValue, StandardCharsets.UTF_8));
        }

        matcher.appendTail(finalUrl); // Append the remaining part of URL after last placeholder
        return finalUrl.toString();
    }

    // -------------------------------------------------------
    // This method converts the vendor's raw API response
    // into our own standard format (Stableresponse)
    //
    // HOW IT WORKS:
    // 1. The mapping object tells us: our field "make" = vendor field "vehicleMake"
    // 2. We search vendor's JSON for "vehicleMake"
    // 3. We store the found value under our key "make"
    // -------------------------------------------------------
    private Stableresponse mapVendorResponse(Map<String, Object> rawVendorResponse,
                                             List<Vehicleresponcemapping> responseMappings) {

        Stableresponse response = new Stableresponse();
        Map<String, String> vehicleDetailsMap = new HashMap<>(); // Will hold our final key-value data

        // If no mappings configured, return empty response
        if (responseMappings.isEmpty()) return response;

        // Take the first mapping row (contains all field name configurations)
        Vehicleresponcemapping mappingConfig = responseMappings.get(0);

        try {
            // Convert the vendor response Map into a JSON tree so we can search it easily
            ObjectMapper jsonConverter = new ObjectMapper();
            JsonNode rootJsonNode = jsonConverter.convertValue(rawVendorResponse, JsonNode.class);

            // Try to find a node called "vehicleinfo" in the JSON (some vendors wrap data inside it)
            JsonNode vehicleInfoNode = findNodeByKey(rootJsonNode, "vehicleinfo");

            // If "vehicleinfo" exists, search inside it; otherwise search from the root
            JsonNode searchStartNode = vehicleInfoNode != null ? vehicleInfoNode : rootJsonNode;

            // Loop through every field in the mapping config class using Reflection
            // Reflection = Java's way of reading a class's fields at runtime without hardcoding names
            for (Field mappingField : mappingConfig.getClass().getDeclaredFields()) {

                // Skip these internal/database fields, they are not vehicle data
                if (List.of("id", "apiId", "vendorId", "countryId")
                        .contains(mappingField.getName())) continue;

                mappingField.setAccessible(true); // Allow reading private fields

                String ourFieldName = mappingField.getName();          // Our internal name e.g., "make"
                Object vendorFieldNameObj = mappingField.get(mappingConfig); // Vendor's name e.g., "vehicleMake"

                if (vendorFieldNameObj != null) {
                    String vendorFieldName = vendorFieldNameObj.toString();

                    // Search the vendor's JSON for the vendor's field name
                    String foundValue = findValue(searchStartNode, vendorFieldName);

                    // If value found, store it with our internal field name as key
                    if (foundValue != null) {
                        vehicleDetailsMap.put(ourFieldName, foundValue);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); // Print error if something goes wrong
        }

        response.setVehicleDetails(vehicleDetailsMap);
        return response;
    }

    // -------------------------------------------------------
    // This method searches a JSON tree to find a VALUE by key name
    // It goes deep into nested objects and arrays (recursive search)
    //
    // Example JSON:
    // { "vehicle": { "make": "Toyota", "color": "Red" } }
    // findValue(root, "make") → returns "Toyota"
    // -------------------------------------------------------
    private String findValue(JsonNode currentNode, String targetKey) {

        if (currentNode == null) return null; // Nothing to search

        if (currentNode.isObject()) {
            // Loop through each key-value pair in this JSON object
            Iterator<Map.Entry<String, JsonNode>> allFields = currentNode.fields();

            while (allFields.hasNext()) {
                Map.Entry<String, JsonNode> singleField = allFields.next();

                // Check if this field's key matches what we're looking for (case-insensitive)
                // Also make sure the value is a simple value (not another object/array)
                if (singleField.getKey().equalsIgnoreCase(targetKey)
                        && singleField.getValue().isValueNode()) {
                    return singleField.getValue().asText(); // Found it! Return the value
                }

                // Not found here → go deeper into this field's value (recursion)
                String deepSearchResult = findValue(singleField.getValue(), targetKey);
                if (deepSearchResult != null) return deepSearchResult; // Found in a nested level
            }
        }

        else if (currentNode.isArray()) {
            // If the current node is a list/array, search inside each item
            for (JsonNode arrayItem : currentNode) {
                String deepSearchResult = findValue(arrayItem, targetKey);
                if (deepSearchResult != null) return deepSearchResult;
            }
        }

        return null; // Not found anywhere in this node
    }

    // -------------------------------------------------------
    // This method searches a JSON tree to find a NODE (object/array) by key name
    // Similar to findValue, but returns the whole node, not just a string value
    //
    // Example JSON:
    // { "vehicleinfo": { "make": "Toyota" } }
    // findNodeByKey(root, "vehicleinfo") → returns { "make": "Toyota" }
    // -------------------------------------------------------
    private JsonNode findNodeByKey(JsonNode currentNode, String targetKey) {

        if (currentNode == null) return null;

        if (currentNode.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> allFields = currentNode.fields();

            while (allFields.hasNext()) {
                Map.Entry<String, JsonNode> singleField = allFields.next();

                // If key matches → return this entire node
                if (singleField.getKey().equalsIgnoreCase(targetKey)) {
                    return singleField.getValue();
                }

                // Not found here → go deeper (recursion)
                JsonNode deepSearchResult = findNodeByKey(singleField.getValue(), targetKey);
                if (deepSearchResult != null) return deepSearchResult;
            }
        }

        else if (currentNode.isArray()) {
            // Search inside each array item
            for (JsonNode arrayItem : currentNode) {
                JsonNode deepSearchResult = findNodeByKey(arrayItem, targetKey);
                if (deepSearchResult != null) return deepSearchResult;
            }
        }

        return null; // Not found
    }

    // -------------------------------------------------------
    // This method converts any object's fields into a simple Map
    // It uses Reflection to read fields without knowing the class at compile time
    //
    // Example: Stablerequest { country="IN", vendorname="ABC" }
    // → { "country": "IN", "vendorname": "ABC" }
    // -------------------------------------------------------
    private Map<String, String> convertRequestToMap(Object anyObject) {
        Map<String, String> resultMap = new HashMap<>();

        try {
            // Get all fields of the object's class (using Reflection)
            for (Field field : anyObject.getClass().getDeclaredFields()) {
                field.setAccessible(true);            // Allow reading even private fields
                Object fieldValue = field.get(anyObject); // Get the value of this field

                if (fieldValue != null) {
                    // Put fieldName → fieldValue as String into the map
                    resultMap.put(field.getName(), fieldValue.toString());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return resultMap;
    }

    // -------------------------------------------------------
    // This is a helper method to find a value in a Map by key,
    // but ignoring UPPER/lower case differences
    //
    // Example: map has key "CountryCode", but we search for "countrycode"
    // Normal map.get() would return null, but this method finds it correctly
    // -------------------------------------------------------
    private String getIgnoreCase(Map<String, String> map, String searchKey) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(searchKey)) {
                return entry.getValue(); // Found a matching key (case-insensitive)
            }
        }
        return null; // No matching key found
    }
}