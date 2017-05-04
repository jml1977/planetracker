drop table raw_data;

create table raw_data (
  message_type text,
  transmission_type smallint,
  session_id integer,
  aircraft_id integer,
  hex_ident text,
  flight_id integer,
  generated_date date,
  generated_time time,
  logged_date date,
  logged_time time,
  callsign text,
  altitude integer /* hopefully unsigned! :) */,
  ground_speed real,
  track real,
  lat real,
  lon real,
  vertical_rate real,
  squawk text,
  alert boolean,
  emergency boolean,
  spi boolean,
  is_on_ground integer
);

create index hex_ident_idx on raw_data(hex_ident);
create index message_type_idx on raw_data (message_type);
