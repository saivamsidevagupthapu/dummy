-- Insert the given GET APIs into your existing tables:
-- - vendorentity(vendor_id, vendor_name, ...)
-- - vendor_apis(api_id, vendor_id, api_url, Api_type, http_method, content_type)
--
-- IMPORTANT: Make sure a row exists in vendorentity for vendor_name = 'VAMSI'

SET @vendor_id := (SELECT vendor_id FROM vendorentity WHERE vendor_name = 'VAMSI' LIMIT 1);

-- If @vendor_id is NULL, create the vendorentity row first, then re-run this script.

INSERT INTO vendor_apis (vendor_id, api_url, Api_type, http_method, content_type)
SELECT @vendor_id, v.api_url, v.api_type, v.http_method, v.content_type
FROM (
  SELECT '/vehicle/{vendorname}/{vehiclenumber}' AS api_url, 'VehicleInfo' AS api_type, 'GET' AS http_method, 'application/json' AS content_type
  UNION ALL
  SELECT '/vehicle/detailsByVendor', 'VehicleInfo', 'GET', 'application/json'
  UNION ALL
  SELECT '/vehicle/vechidetails', 'VehicleInfo', 'GET', 'application/json'
) v
WHERE @vendor_id IS NOT NULL
  AND NOT EXISTS (
    SELECT 1
    FROM vendor_apis va
    WHERE va.vendor_id = @vendor_id
      AND va.http_method = v.http_method
      AND va.api_url = v.api_url
  );

