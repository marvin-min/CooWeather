create table provinces(
    id integer primary key autoincrement,
    name text,
    code text
);

create table cities(
    id integer primary key autoincrement,
    name text,
    code text,
    province_code
)

create table districts(
    id integer primary key autoincrement,
    name text,
    code text,
    province_code,
    city_code,
    city_en
)
