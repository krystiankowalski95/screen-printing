INSERT INTO public.order_statuses (id, status_name) VALUES (1, 'created');
INSERT INTO public.order_statuses (id, status_name) VALUES (2, 'paid');
INSERT INTO public.order_statuses (id, status_name) VALUES (3, 'pending');
INSERT INTO public.order_statuses (id, status_name) VALUES (4, 'cancelled');
INSERT INTO public.order_statuses (id, status_name) VALUES (5, 'completed');

INSERT INTO public.categories (id, category_name) VALUES (1, 'emulsje');
INSERT INTO public.categories (id, category_name) VALUES (2, 'siatki');
INSERT INTO public.categories (id, category_name) VALUES (3, 'ramy');

INSERT INTO public.products (id, name, price, is_active, category, version, stock, description) VALUES (4, 'Aluminiowa 55', 5, true, 'ramy', 6, 0, 'Aluminowa rama o wymiarach 55x55cm. Gwarancja na produkt 24 miesiące.');
INSERT INTO public.products (id, name, price, is_active, category, version, stock, description) VALUES (1, 'Sati 24T', 60, true, 'siatki', 44, 31, 'Siatka do sitodruku wysokiej jakości. Kolor biały. Podana cena jest za 1 mb. ');
INSERT INTO public.products (id, name, price, is_active, category, version, stock, description) VALUES (7, 'Farba czarna wodna', 29, true, 'emulsje', 3, 2, 'Czarna farba wodna. Idealnie nadaje się do drukowania na wszelkiego rodzaju opakowaniach na żywność.');
INSERT INTO public.products (id, name, price, is_active, category, version, stock, description) VALUES (3, 'Aluminiowa 60', 149.59, true, 'ramy', 12, 1, 'Aluminowa rama o wymiarach 60x60cm. Gwarancja na produkt 24 miesiące.');
INSERT INTO public.products (id, name, price, is_active, category, version, stock, description) VALUES (5, 'Farba zielona do ubrań', 39.99, false, 'emulsje', 2, 4, 'Nowa farba koloru zielonego do tekstyliów wszelkiego rodzaju.');
INSERT INTO public.products (id, name, price, is_active, category, version, stock, description) VALUES (6, 'Farba czerwona wodna', 39, false, 'emulsje', 2, 1, 'Czerwona farba wodna. Idealnie nadaje się do drukowania na wszelkiego rodzaju opakowaniach na żywność.');
INSERT INTO public.products (id, name, price, is_active, category, version, stock, description) VALUES (2, 'Sati 60T Żółta', 110, true, 'siatki', 13, 60, 'Żółta siatka do sitodruku. Idealna do mały i precyzyjnych nadruków.');

INSERT INTO public.login_data (id, username, password, active, confirmed, token, version, is_token_used, register_lang, password_token, token_creation_date, password_token_creation_date, is_password_token_used) VALUES (1, 'client', '$2a$10$QODR9Lkeu2SQ7UkpRLqcXOX/BF9qcVlsAqflMM8jQgPzFoVV4DtCO', true, true, '4b4aa8177a174d60a527329de06642a1', 56, false, 'en', '9782cc0b98c746098bf54754720a80b6', '2021-02-07 14:56:15.876729', '2021-02-11 12:38:15.595984', false);
INSERT INTO public.login_data (id, username, password, active, confirmed, token, version, is_token_used, register_lang, password_token, token_creation_date, password_token_creation_date, is_password_token_used) VALUES (2, 'employee', '$2a$10$MMvwjPm1uMwMIv2lPFw1DuYMxWCKj4uSYCKQuC0AFTxdmMM/pADOu', true, true, '59127b50a4b84f1c8a45e085afd115fe', 7, true, 'pl', null, null, null, false);
INSERT INTO public.login_data (id, username, password, active, confirmed, token, version, is_token_used, register_lang, password_token, token_creation_date, password_token_creation_date, is_password_token_used) VALUES (3, 'admin', '$2a$10$fuxZA5GtSSE/AL6hOQPOvul.xIXmjX/LF2MzwDsBB4YxBaTAZqPUy', true, true, 'ad3941cc5207418faf246c9ca610f612', 14, true, 'en', null, null, null, false);

INSERT INTO public.user_personal_data (id, firstname, lastname, email, phone_number, version) VALUES (1, 'Zuzanna', 'Sanah', 'ikriss95@gmail.com', '123456786', 1);
INSERT INTO public.user_personal_data (id, firstname, lastname, email, phone_number, version) VALUES (2, 'Zuzanna', 'Jurczak', 'dldmmhznjdvspenybz@twzhhq.online', '600150601', 1);
INSERT INTO public.user_personal_data (id, firstname, lastname, email, phone_number, version) VALUES (3, 'John', 'Doe', 'csspcmmwppohhpenja@twzhhq.com', '609431325', 1);

INSERT INTO public.user_access_level (id, access_level_name, user_id, active, version) VALUES (1, 'ROLE_CLIENT', 1, true, 1);
INSERT INTO public.user_access_level (id, access_level_name, user_id, active, version) VALUES (4, 'ROLE_CLIENT', 2, true, 1);
INSERT INTO public.user_access_level (id, access_level_name, user_id, active, version) VALUES (6, 'ROLE_ADMIN', 2, false, 1);
INSERT INTO public.user_access_level (id, access_level_name, user_id, active, version) VALUES (7, 'ROLE_CLIENT', 3, false, 1);
INSERT INTO public.user_access_level (id, access_level_name, user_id, active, version) VALUES (2, 'ROLE_EMPLOYEE', 1, false, 1);
INSERT INTO public.user_access_level (id, access_level_name, user_id, active, version) VALUES (3, 'ROLE_ADMIN', 1, false, 1);
INSERT INTO public.user_access_level (id, access_level_name, user_id, active, version) VALUES (9, 'ROLE_ADMIN', 3, true, 1);
INSERT INTO public.user_access_level (id, access_level_name, user_id, active, version) VALUES (5, 'ROLE_EMPLOYEE', 2, true, 1);
INSERT INTO public.user_access_level (id, access_level_name, user_id, active, version) VALUES (8, 'ROLE_EMPLOYEE', 3, false, 1);
