ALTER TABLE users
ALTER COLUMN address_country
TYPE VARCHAR(2)
USING RTRIM(address_country)::VARCHAR(2);