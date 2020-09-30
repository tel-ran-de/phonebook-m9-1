ALTER TABLE public.phone
    ALTER COLUMN country_code SET DATA TYPE character varying(255) COLLATE pg_catalog."default";
ALTER TABLE public.phone
    ALTER COLUMN phone_number SET DATA TYPE character varying(255) COLLATE pg_catalog."default";

