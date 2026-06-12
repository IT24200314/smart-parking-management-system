ALTER TABLE bookings
    ADD COLUMN ticket_code VARCHAR(80),
    ADD COLUMN qr_payload VARCHAR(1000),
    ADD COLUMN qr_code_base64 TEXT,
    ADD COLUMN checked_in_at TIMESTAMPTZ,
    ADD COLUMN checked_out_at TIMESTAMPTZ;

UPDATE bookings
SET ticket_code = 'SPP-' || upper(substring(id::text from 1 for 8)),
    qr_payload = 'SMARTPARKPRO:' || id::text,
    qr_code_base64 = ''
WHERE ticket_code IS NULL;

ALTER TABLE bookings
    ALTER COLUMN ticket_code SET NOT NULL,
    ALTER COLUMN qr_payload SET NOT NULL,
    ALTER COLUMN qr_code_base64 SET NOT NULL,
    ADD CONSTRAINT uk_bookings_ticket_code UNIQUE (ticket_code);
