-- one ordinary user
INSERT
  INTO users (id, email, password, email_validated )
  VALUES (
    '62ff5258-8976-11ec-b58c-e35f5b1fc926',
    'user@dancier.net',
    '$2a$10$GOChyBEqco9m3wZwkh0RqOTwyWq4HmocguPPfEraSgnbmlrM4.Fey',
     true
  );

INSERT
  INTO user_roles (user_id, role_id)
SELECT '62ff5258-8976-11ec-b58c-e35f5b1fc926',
       id
  FROM roles
 WHERE name = 'ROLE_USER';

-- one admin
INSERT
  INTO users (id, email, password, email_validated )
  VALUES (
    '21df1a30-89a6-11ec-b4cf-67ea17ff4219',
    'admin@dancier.net',
    '$2a$10$GOChyBEqco9m3wZwkh0RqOTwyWq4HmocguPPfEraSgnbmlrM4.Fey',
     true
  );

INSERT
  INTO user_roles (user_id, role_id)
SELECT '21df1a30-89a6-11ec-b4cf-67ea17ff4219',
       id
  FROM roles
 WHERE name = 'ROLE_USER';

INSERT
  INTO dancer (id, user_id, dancer_name, city)
VALUES (
  '00000000-0000-0000-0000-000000000002',
  '62ff5258-8976-11ec-b58c-e35f5b1fc926',
  'dancero',
  'Dortmund'
);
