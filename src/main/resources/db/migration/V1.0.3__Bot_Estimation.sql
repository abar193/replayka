create table replayka.login_ip
(
    login_id integer not null
        constraint login_ip_social_login_id_fk
            references replayka.social_login,
    ip       inet
);

CREATE FUNCTION replayka.is_bot(IN header JSONB)
    RETURNS boolean
    LANGUAGE sql
    IMMUTABLE
    RETURNS NULL ON NULL INPUT
RETURN ((header -> 'headers' -> 'User-agent')::text) ilike '%bot%';

CREATE FUNCTION replayka.is_owner(IN header JSONB)
    RETURNS BOOLEAN
    IMMUTABLE
    RETURNS NULL ON NULL INPUT AS $$
BEGIN
    return false; -- Reserved for later
END;
$$ LANGUAGE plpgsql;

-- Categorize user into one of 3 categories:
-- Category 0: Normal user;
-- Category 1: Web crawler;
-- Category 2: Blog owner (visits his own blog);
CREATE OR REPLACE FUNCTION replayka.visitor_category(IN header JSONB)
    RETURNS INT
    IMMUTABLE AS $$
BEGIN
    if replayka.is_bot(header) then return 1; end if;
    if replayka.is_owner(header) then return 2; end if;
    return 0;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION replayka.next_visitor_state(INOUT state INTEGER[3], IN header JSONB)
AS $$
DECLARE pos INT;
BEGIN
    pos := replayka.visitor_category(header) + 1; -- Arrays start at 1
    state[pos] := state[pos] + 1;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE AGGREGATE replayka.visitor_categories(in header JSONB) (
    sfunc = replayka.next_visitor_state,
    stype = int[3],
    initcond = '{0,0,0}'
);