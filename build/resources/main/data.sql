INSERT INTO address (address_id,street,city,country)
VALUES ('197928f9-da7d-487f-911f-bdfd22debbcb', 'Sneakers', 'Style over substance.', 'test'),
       ('596493b5-c6cc-4e72-8a78-f9a5f3f277df', 'Durian','Banned on most commercial flights!', 'test'),
       ('1388244c-79af-425b-a976-547c4e542750', 'Crystal Pepsi', 'hell', 'tes')
ON CONFLICT DO NOTHING;

INSERT INTO retail_store (store_id, name, area, opening_hours, address_id)
VALUES
    ('3148818b-aa58-4edc-a71b-06d26d7165e5', 'Store A', 'Area 1', '09:00-18:00',(SELECT address_id FROM address WHERE address.street = 'Sneakers')),
    ('2d0b0417-5a8b-4633-8e9d-bfa3ed0d67a6', 'Store B', 'Area 2', '10:00-20:00', '596493b5-c6cc-4e72-8a78-f9a5f3f277df'),
    ('bc2b1c90-e572-4c9f-a7b1-5a65091d6c90', 'Store C', 'Area 3', '08:00-17:00', '1388244c-79af-425b-a976-547c4e542750')
ON CONFLICT DO NOTHING;
