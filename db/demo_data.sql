-- Demo dáta
USE dodo_pizza;

DELETE FROM order_item_extras;
DELETE FROM order_items;
DELETE FROM orders;
DELETE FROM pizza_tags;
DELETE FROM pizza_ingredients;
DELETE FROM tags;
DELETE FROM ingredients;
DELETE FROM pizza_sizes;
DELETE FROM pizzas;
DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM roles;

-- Roly
INSERT INTO roles (name, display_name_sk) VALUES
('ROLE_ANONYMOUS', 'Anonymný používateľ'),
('ROLE_CUSTOMER', 'Zákazník'),
('ROLE_KITCHEN', 'Kuchár / manažér'),
('ROLE_COURIER', 'Kuriér'),
('ROLE_ADMIN', 'Admin / majiteľ');

-- Používatelia (heslá sú dočasné hodnoty, v aplikácii sa budú ukladať ako hash)
INSERT INTO users (email, password_hash, first_name, last_name, phone, delivery_address, profile_image_url, enabled) VALUES
('zakaznik@test.sk', 'temp', 'Peter', 'Novák', '+421900111222', 'Nitra, Štúrova 10', '/img/profiles/user1.jpg', TRUE),
('kuchar@test.sk', 'temp', 'Marek', 'Kováč', '+421900333444', 'Nitra, Mostná 5', '/img/profiles/user2.jpg', TRUE),
('kurier@test.sk', 'temp', 'Ján', 'Horváth', '+421900555666', 'Nitra, Chrenová 2', '/img/profiles/user3.jpg', TRUE),
('admin@test.sk', 'temp', 'Eva', 'Šimeková', '+421900777888', 'Nitra, Centrum 1', '/img/profiles/user4.jpg', TRUE);

-- Priradenie rolí používateľom
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ROLE_CUSTOMER'
WHERE u.email = 'zakaznik@test.sk';

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ROLE_KITCHEN'
WHERE u.email = 'kuchar@test.sk';

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ROLE_COURIER'
WHERE u.email = 'kurier@test.sk';

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ROLE_ADMIN'
WHERE u.email = 'admin@test.sk';

-- Tagy
INSERT INTO tags (name_sk, slug) VALUES
('Vegetariánska', 'vegetarianska'),
('Pikantná', 'pikantna'),
('Novinka', 'novinka'),
('Bestseller', 'bestseller'),
('Bezlepková', 'bezlepkova');

-- Ingrediencie (15 ks)
INSERT INTO ingredients (name_sk, allergen_info_sk, vegan, spicy, extra_price_eur) VALUES
('Mozzarella', '7', FALSE, FALSE, 1.20),
('Parmezán', '7', FALSE, FALSE, 1.50),
('Šunka', '1', FALSE, FALSE, 1.50),
('Saláma', '1', FALSE, FALSE, 1.50),
('Slanina', '1', FALSE, FALSE, 1.70),
('Kuracie mäso', NULL, FALSE, FALSE, 1.80),
('Tuniak', '4', FALSE, FALSE, 1.80),
('Olivy', NULL, TRUE, FALSE, 1.00),
('Kukurica', NULL, TRUE, FALSE, 0.90),
('Šampiňóny', NULL, TRUE, FALSE, 1.00),
('Cibuľa', NULL, TRUE, FALSE, 0.70),
('Paprika', NULL, TRUE, FALSE, 0.80),
('Jalapeño', NULL, TRUE, TRUE, 0.90),
('Ananás', NULL, TRUE, FALSE, 1.00),
('Rukola', NULL, TRUE, FALSE, 1.00);

-- Pizze (10 ks)
INSERT INTO pizzas (name_sk, description_sk, slug, image_url, active) VALUES
('Margherita', 'Paradajkový základ, mozzarella, bazalka.', 'margherita', '/img/pizzas/margherita.jpg', TRUE),
('Šunková', 'Paradajkový základ, mozzarella, šunka.', 'sunkova', '/img/pizzas/sunkova.jpg', TRUE),
('Salámová', 'Paradajkový základ, mozzarella, saláma.', 'salamova', '/img/pizzas/salamova.jpg', TRUE),
('Hawai', 'Paradajkový základ, mozzarella, šunka, ananás.', 'hawai', '/img/pizzas/hawai.jpg', TRUE),
('Quattro Formaggi', 'Mozzarella, parmezán a ďalšie syry.', 'quattro-formaggi', '/img/pizzas/quattro-formaggi.jpg', TRUE),
('Funghi', 'Paradajkový základ, mozzarella, šampiňóny.', 'funghi', '/img/pizzas/funghi.jpg', TRUE),
('Vegetariánska', 'Zelenina, mozzarella, olivy.', 'vegetarianska', '/img/pizzas/vegetarianska.jpg', TRUE),
('Diavola', 'Pikantná saláma, jalapeño, mozzarella.', 'diavola', '/img/pizzas/diavola.jpg', TRUE),
('Tuniaková', 'Paradajkový základ, mozzarella, tuniak, cibuľa.', 'tuniakova', '/img/pizzas/tuniakova.jpg', TRUE),
('Kuracia BBQ', 'BBQ základ, mozzarella, kuracie mäso, cibuľa.', 'kuracia-bbq', '/img/pizzas/kuracia-bbq.jpg', TRUE);

-- Veľkosti pre každú pizzu (S/M/L)
INSERT INTO pizza_sizes (pizza_id, size_code, diameter_cm, price_eur, available)
SELECT p.id, 'S', 25,
  CASE p.slug
    WHEN 'margherita' THEN 6.50
    WHEN 'sunkova' THEN 7.20
    WHEN 'salamova' THEN 7.20
    WHEN 'hawai' THEN 7.80
    WHEN 'quattro-formaggi' THEN 8.20
    WHEN 'funghi' THEN 7.10
    WHEN 'vegetarianska' THEN 7.30
    WHEN 'diavola' THEN 7.90
    WHEN 'tuniakova' THEN 8.10
    WHEN 'kuracia-bbq' THEN 8.30
    ELSE 7.00
  END,
  TRUE
FROM pizzas p;

INSERT INTO pizza_sizes (pizza_id, size_code, diameter_cm, price_eur, available)
SELECT p.id, 'M', 30,
  CASE p.slug
    WHEN 'margherita' THEN 8.50
    WHEN 'sunkova' THEN 9.20
    WHEN 'salamova' THEN 9.20
    WHEN 'hawai' THEN 9.90
    WHEN 'quattro-formaggi' THEN 10.40
    WHEN 'funghi' THEN 9.10
    WHEN 'vegetarianska' THEN 9.30
    WHEN 'diavola' THEN 10.10
    WHEN 'tuniakova' THEN 10.30
    WHEN 'kuracia-bbq' THEN 10.60
    ELSE 9.00
  END,
  TRUE
FROM pizzas p;

INSERT INTO pizza_sizes (pizza_id, size_code, diameter_cm, price_eur, available)
SELECT p.id, 'L', 35,
  CASE p.slug
    WHEN 'margherita' THEN 10.50
    WHEN 'sunkova' THEN 11.40
    WHEN 'salamova' THEN 11.40
    WHEN 'hawai' THEN 12.20
    WHEN 'quattro-formaggi' THEN 12.80
    WHEN 'funghi' THEN 11.20
    WHEN 'vegetarianska' THEN 11.40
    WHEN 'diavola' THEN 12.40
    WHEN 'tuniakova' THEN 12.60
    WHEN 'kuracia-bbq' THEN 13.00
    ELSE 11.00
  END,
  TRUE
FROM pizzas p;

-- Prepojenie pizze - tagy
INSERT INTO pizza_tags (pizza_id, tag_id)
SELECT p.id, t.id
FROM pizzas p
JOIN tags t ON t.slug = 'bestseller'
WHERE p.slug IN ('margherita', 'sunkova', 'diavola');

INSERT INTO pizza_tags (pizza_id, tag_id)
SELECT p.id, t.id
FROM pizzas p
JOIN tags t ON t.slug = 'vegetarianska'
WHERE p.slug IN ('margherita', 'vegetarianska', 'funghi');

INSERT INTO pizza_tags (pizza_id, tag_id)
SELECT p.id, t.id
FROM pizzas p
JOIN tags t ON t.slug = 'pikantna'
WHERE p.slug IN ('diavola');

INSERT INTO pizza_tags (pizza_id, tag_id)
SELECT p.id, t.id
FROM pizzas p
JOIN tags t ON t.slug = 'novinka'
WHERE p.slug IN ('kuracia-bbq');

-- Základné recepty (pizza_ingredients)
INSERT INTO pizza_ingredients (pizza_id, ingredient_id)
SELECT p.id, i.id
FROM pizzas p
JOIN ingredients i ON i.name_sk IN ('Mozzarella')
WHERE p.slug IN ('margherita','sunkova','salamova','hawai','funghi','vegetarianska','diavola','tuniakova','kuracia-bbq','quattro-formaggi');

INSERT INTO pizza_ingredients (pizza_id, ingredient_id)
SELECT p.id, i.id
FROM pizzas p
JOIN ingredients i ON i.name_sk = 'Šunka'
WHERE p.slug IN ('sunkova','hawai');

INSERT INTO pizza_ingredients (pizza_id, ingredient_id)
SELECT p.id, i.id
FROM pizzas p
JOIN ingredients i ON i.name_sk = 'Saláma'
WHERE p.slug IN ('salamova','diavola');

INSERT INTO pizza_ingredients (pizza_id, ingredient_id)
SELECT p.id, i.id
FROM pizzas p
JOIN ingredients i ON i.name_sk IN ('Šampiňóny')
WHERE p.slug IN ('funghi');

INSERT INTO pizza_ingredients (pizza_id, ingredient_id)
SELECT p.id, i.id
FROM pizzas p
JOIN ingredients i ON i.name_sk IN ('Olivy','Paprika','Cibuľa','Kukurica')
WHERE p.slug IN ('vegetarianska');

INSERT INTO pizza_ingredients (pizza_id, ingredient_id)
SELECT p.id, i.id
FROM pizzas p
JOIN ingredients i ON i.name_sk IN ('Tuniak','Cibuľa')
WHERE p.slug IN ('tuniakova');

INSERT INTO pizza_ingredients (pizza_id, ingredient_id)
SELECT p.id, i.id
FROM pizzas p
JOIN ingredients i ON i.name_sk IN ('Kuracie mäso','Cibuľa')
WHERE p.slug IN ('kuracia-bbq');

INSERT INTO pizza_ingredients (pizza_id, ingredient_id)
SELECT p.id, i.id
FROM pizzas p
JOIN ingredients i ON i.name_sk IN ('Parmezán')
WHERE p.slug IN ('quattro-formaggi');

-- Objednávky (5 ks v rôznych stavoch)
INSERT INTO orders (user_id, order_number, status, delivery_address, customer_note, total_price_eur)
SELECT u.id, 'ORD-2026-0001', 'pending', 'Nitra, Štúrova 10', 'Prosím bez cibule.', 0.00
FROM users u WHERE u.email = 'zakaznik@test.sk';

INSERT INTO orders (user_id, order_number, status, delivery_address, customer_note, total_price_eur)
SELECT u.id, 'ORD-2026-0002', 'preparing', 'Nitra, Štúrova 10', NULL, 0.00
FROM users u WHERE u.email = 'zakaznik@test.sk';

INSERT INTO orders (user_id, order_number, status, delivery_address, customer_note, total_price_eur)
SELECT u.id, 'ORD-2026-0003', 'ready', 'Nitra, Štúrova 10', 'Zazvoňte prosím.', 0.00
FROM users u WHERE u.email = 'zakaznik@test.sk';

INSERT INTO orders (user_id, order_number, status, delivery_address, customer_note, total_price_eur)
SELECT u.id, 'ORD-2026-0004', 'delivering', 'Nitra, Štúrova 10', NULL, 0.00
FROM users u WHERE u.email = 'zakaznik@test.sk';

INSERT INTO orders (user_id, order_number, status, delivery_address, customer_note, total_price_eur)
SELECT u.id, 'ORD-2026-0005', 'delivered', 'Nitra, Štúrova 10', NULL, 0.00
FROM users u WHERE u.email = 'zakaznik@test.sk';

-- Položky objednávok (2 položky na niektoré objednávky)
-- ORD-2026-0001: Margherita M x1 + extra Parmezán x1
INSERT INTO order_items (order_id, size_id, pizza_name_snapshot, size_snapshot, diameter_snapshot_cm, unit_price_snapshot_eur, quantity, line_total_eur)
SELECT o.id, ps.id, 'Margherita', 'M', 30, ps.price_eur, 1, ps.price_eur
FROM orders o
JOIN pizzas p ON p.slug = 'margherita'
JOIN pizza_sizes ps ON ps.pizza_id = p.id AND ps.size_code = 'M'
WHERE o.order_number = 'ORD-2026-0001';

INSERT INTO order_item_extras (order_item_id, ingredient_id, ingredient_name_snapshot, extra_price_snapshot_eur, quantity, line_total_eur)
SELECT oi.id, i.id, i.name_sk, i.extra_price_eur, 1, i.extra_price_eur
FROM order_items oi
JOIN orders o ON o.id = oi.order_id AND o.order_number = 'ORD-2026-0001'
JOIN ingredients i ON i.name_sk = 'Parmezán'
LIMIT 1;

-- ORD-2026-0002: Diavola L x1 + extra Jalapeño x2
INSERT INTO order_items (order_id, size_id, pizza_name_snapshot, size_snapshot, diameter_snapshot_cm, unit_price_snapshot_eur, quantity, line_total_eur)
SELECT o.id, ps.id, 'Diavola', 'L', 35, ps.price_eur, 1, ps.price_eur
FROM orders o
JOIN pizzas p ON p.slug = 'diavola'
JOIN pizza_sizes ps ON ps.pizza_id = p.id AND ps.size_code = 'L'
WHERE o.order_number = 'ORD-2026-0002';

INSERT INTO order_item_extras (order_item_id, ingredient_id, ingredient_name_snapshot, extra_price_snapshot_eur, quantity, line_total_eur)
SELECT oi.id, i.id, i.name_sk, i.extra_price_eur, 2, (i.extra_price_eur * 2)
FROM order_items oi
JOIN orders o ON o.id = oi.order_id AND o.order_number = 'ORD-2026-0002'
JOIN ingredients i ON i.name_sk = 'Jalapeño'
LIMIT 1;

-- ORD-2026-0003: Hawai S x2 (bez extra)
INSERT INTO order_items (order_id, size_id, pizza_name_snapshot, size_snapshot, diameter_snapshot_cm, unit_price_snapshot_eur, quantity, line_total_eur)
SELECT o.id, ps.id, 'Hawai', 'S', 25, ps.price_eur, 2, (ps.price_eur * 2)
FROM orders o
JOIN pizzas p ON p.slug = 'hawai'
JOIN pizza_sizes ps ON ps.pizza_id = p.id AND ps.size_code = 'S'
WHERE o.order_number = 'ORD-2026-0003';

-- ORD-2026-0004: Tuniaková M x1 + extra Olivy x1
INSERT INTO order_items (order_id, size_id, pizza_name_snapshot, size_snapshot, diameter_snapshot_cm, unit_price_snapshot_eur, quantity, line_total_eur)
SELECT o.id, ps.id, 'Tuniaková', 'M', 30, ps.price_eur, 1, ps.price_eur
FROM orders o
JOIN pizzas p ON p.slug = 'tuniakova'
JOIN pizza_sizes ps ON ps.pizza_id = p.id AND ps.size_code = 'M'
WHERE o.order_number = 'ORD-2026-0004';

INSERT INTO order_item_extras (order_item_id, ingredient_id, ingredient_name_snapshot, extra_price_snapshot_eur, quantity, line_total_eur)
SELECT oi.id, i.id, i.name_sk, i.extra_price_eur, 1, i.extra_price_eur
FROM order_items oi
JOIN orders o ON o.id = oi.order_id AND o.order_number = 'ORD-2026-0004'
JOIN ingredients i ON i.name_sk = 'Olivy'
LIMIT 1;

-- ORD-2026-0005: Kuracia BBQ M x1 + extra Slanina x1
INSERT INTO order_items (order_id, size_id, pizza_name_snapshot, size_snapshot, diameter_snapshot_cm, unit_price_snapshot_eur, quantity, line_total_eur)
SELECT o.id, ps.id, 'Kuracia BBQ', 'M', 30, ps.price_eur, 1, ps.price_eur
FROM orders o
JOIN pizzas p ON p.slug = 'kuracia-bbq'
JOIN pizza_sizes ps ON ps.pizza_id = p.id AND ps.size_code = 'M'
WHERE o.order_number = 'ORD-2026-0005';

INSERT INTO order_item_extras (order_item_id, ingredient_id, ingredient_name_snapshot, extra_price_snapshot_eur, quantity, line_total_eur)
SELECT oi.id, i.id, i.name_sk, i.extra_price_eur, 1, i.extra_price_eur
FROM order_items oi
JOIN orders o ON o.id = oi.order_id AND o.order_number = 'ORD-2026-0005'
JOIN ingredients i ON i.name_sk = 'Slanina'
LIMIT 1;

-- Prepočet total_price_eur podľa položiek a extra prísad
UPDATE orders o
SET o.total_price_eur =
  (
    SELECT IFNULL(SUM(oi.line_total_eur), 0)
    FROM order_items oi
    WHERE oi.order_id = o.id
  )
  +
  (
    SELECT IFNULL(SUM(oe.line_total_eur), 0)
    FROM order_item_extras oe
    JOIN order_items oi2 ON oi2.id = oe.order_item_id
    WHERE oi2.order_id = o.id
  );
