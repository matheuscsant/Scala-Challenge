CREATE DATABASE challenge_scala
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Portuguese_Brazil.1252'
    LC_CTYPE = 'Portuguese_Brazil.1252'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

CREATE TABLE IF NOT EXISTS public.room
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    "number" character varying(20) COLLATE pg_catalog."default",
    type character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT room_pkey PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS public.guest
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    name character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT guest_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.reservation
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    guest_id integer,
    room_id integer,
    check_in timestamp without time zone,
    check_out timestamp without time zone,
    CONSTRAINT reservation_pkey PRIMARY KEY (id),
    CONSTRAINT reservation_guest_id_fkey FOREIGN KEY (guest_id)
        REFERENCES public.guest (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT reservation_room_id_fkey FOREIGN KEY (room_id)
        REFERENCES public.room (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE OR REPLACE VIEW public.view_occupancy
 AS
 SELECT re.id,
    re.guest_id,
    gu.name AS guest_name,
    re.room_id,
    ro.number AS room_number,
    ro.type AS room_type,
    re.check_in,
    re.check_out
   FROM reservation re
     JOIN room ro ON re.room_id = ro.id
     JOIN guest gu ON re.guest_id = gu.id;