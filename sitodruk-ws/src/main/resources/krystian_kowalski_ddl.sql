CREATE USER modullogowania with password 'modullogowania123';
CREATE USER modulklienta with password 'modulklienta123';
CREATE USER modulproduktow with password 'modulproduktow123';
CREATE USER modulzamowien with password 'modulzamowien123';

create table address
(
    id bigserial not null
        constraint address_pk
            primary key,
    country varchar(100) not null,
    voivodeship varchar(100) not null,
    city varchar(100) not null,
    postal_code varchar(10) not null,
    street varchar(100) not null,
    street_number varchar(11) not null,
    version bigint default 1 not null
);

alter table address owner to postgres;

create sequence address_seq;

grant select, update on sequence address_seq to modulzamowien;

grant insert, select, update on address to modulzamowien;

create unique index address_id_uindex
    on address (id);


create table categories
(
    id bigserial not null
        constraint categories_pk
            primary key,
    category_name varchar(100) not null
);

alter table categories owner to postgres;

grant select on categories to modulproduktow;

create unique index categories_id_uindex
    on categories (id);

create unique index categories_category_name_uindex
    on categories (category_name);

create table if not exists login_data
(
    id bigserial not null
        constraint login_data_pk
            primary key,
    username varchar(64) not null,
    password varchar(64) not null,
    active boolean default true not null,
    confirmed boolean default false not null,
    token varchar(32),
    version bigint default 1 not null,
    is_token_used boolean default false,
    register_lang varchar(4) default 'pl'::character varying,
    password_token varchar(32),
    token_creation_date timestamp with time zone,
    password_token_creation_date timestamp with time zone,
    is_password_token_used boolean default false
);

alter table login_data owner to postgres;

create sequence login_data_seq;

grant select, update, usage on sequence login_data_seq to modulklienta;

grant insert, select, update, delete, truncate, references, trigger on login_data to modulklienta;

grant select on login_data to modulzamowien;

grant select on login_data to modullogowania;

create unique index login_data_id_uindex
    on login_data (id);

create unique index login_data_username_uindex
    on login_data (username);

create unique index login_data_token_uindex
    on login_data (token);

create table order_statuses
(
    id bigserial not null
        constraint order_statuses_pk
            primary key,
    status_name varchar(100) not null
);

alter table order_statuses owner to postgres;

grant select on order_statuses to modulzamowien;

create unique index order_statuses_id_uindex
    on order_statuses (id);

create unique index order_statuses_status_name_uindex
    on order_statuses (status_name);

create table orders
(
    id bigserial not null
        constraint orders_pk
            primary key,
    username varchar(64) not null
        constraint orders_login_data_username_fk
            references login_data (username),
    address_id bigint not null
        constraint orders_address_id_fk
            references address
            on update cascade on delete cascade,
    timestamp date default CURRENT_TIMESTAMP,
    order_status varchar(100) not null
        constraint orders_order_statuses_status_name_fk
            references order_statuses (status_name),
    version integer default 1 not null,
    payu_order_id varchar(255) not null,
    total_value double precision default 0.0 not null
);

alter table orders owner to postgres;

grant insert, select, update on orders to modulzamowien;

create unique index orders_id_uindex
    on orders (id);


create table products
(
    id bigserial not null
        constraint products_pk
            primary key,
    name varchar(200) not null,
    price double precision default 0.0 not null,
    is_active boolean default false not null,
    category varchar(100) not null
        constraint products_categories_category_name_fk
            references categories (category_name),
    version bigint default 1 not null,
    stock bigint default 0 not null,
    description varchar(300)
);

alter table products owner to postgres;

grant insert, select, update  on products to modulproduktow;

grant select, update on products to modulzamowien;

create unique index products_id_uindex
    on products (id);

create unique index products_name_uindex
    on products (name);


create table user_access_level
(
    id serial not null
        constraint user_access_level_pk
            primary key,
    access_level_name varchar(64) not null,
    user_id bigint not null
        constraint user_access_level_login_data_id_fk
            references login_data,
    active boolean default false not null,
    version bigint default 1 not null
);

alter table user_access_level owner to postgres;

create sequence user_access_level_seq;

grant select, update, usage on sequence user_access_level_seq to modulklienta;


grant select on user_access_level to modullogowania;

grant select on user_access_level to modulzamowien;

grant insert, select, update, delete, truncate, references, trigger on user_access_level to modulklienta;

create unique index user_access_level_id_uindex
    on user_access_level (id);

create unique index user_access_level_user_id_access_level_name_uindex
    on user_access_level (user_id, access_level_name);

create table user_personal_data
(
    id bigint not null
        constraint user_personal_data_pk
            primary key
        constraint user_personal_data_login_data_id_fk
            references login_data,
    firstname varchar(64) not null,
    lastname varchar(100) not null,
    email varchar(100) not null,
    phone_number varchar(15) not null,
    version bigint default 1 not null
);

alter table user_personal_data owner to postgres;

grant select on user_personal_data to modullogowania;

grant insert, select, update, delete, truncate, references, trigger on user_personal_data to modulklienta;

grant select on user_personal_data to modulzamowien;

create unique index user_personal_data_id_uindex
    on user_personal_data (id);

create unique index user_personal_data_email_uindex
    on user_personal_data (email);

create table order_product
(
    id bigserial not null
        constraint order_product_pk
            primary key,
    order_id bigint not null
        constraint order_product_orders_id_fk
            references orders,
    product_id bigint not null
        constraint order_product_products_id_fk
            references products,
    amount double precision
);

alter table order_product owner to postgres;

grant select, update on sequence order_product_id_seq to modulzamowien;

grant insert, select, update on order_product to modulzamowien;

create unique index order_product_id_uindex
    on order_product (id);


create sequence products_seq;

grant select,update on sequence  products_seq to modulproduktow;

create sequence orders_seq;

grant select,update on sequence  orders_seq to modulzamowien;

create sequence order_product_seq;

grant select,update on sequence  order_product_seq to modulzamowien;











