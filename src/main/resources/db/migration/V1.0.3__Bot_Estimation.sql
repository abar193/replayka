create or replace function replayka.is_bot(IN info jsonb)
    RETURNS boolean
    RETURNS NULL ON NULL INPUT
    IMMUTABLE AS $$
BEGIN
    return ((info -> 'headers' -> 'User-Agent')::text) ilike '%BOT%';
END;
$$ LANGUAGE plpgsql;

create or replace function replayka.is_owner(IN info jsonb)
    RETURNS boolean
    RETURNS NULL ON NULL INPUT
    IMMUTABLE AS $$
BEGIN
    return ((info -> 'headers' -> 'UserType')::text) ilike '%BlogOwner%';
END;
$$ LANGUAGE plpgsql;

-- Categorize visitor into one of 3 categories:
-- Category 0: Normal user;
-- Category 1: Web crawler;
-- Category 2: Blog owner (visits his own blog);
CREATE OR REPLACE FUNCTION replayka.visitor_category(IN info JSONB)
    RETURNS INT
    IMMUTABLE AS $$
BEGIN
    if replayka.is_bot(info) then return 1; end if;
    if replayka.is_owner(info) then return 2; end if;
    return 0;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION replayka.next_visitor_state(INOUT state INTEGER[3], IN info JSONB)
AS $$
DECLARE pos INT;
BEGIN
    pos := replayka.visitor_category(info) + 1; -- Arrays start at 1
    state[pos] := state[pos] + 1;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE AGGREGATE replayka.visitor_categories(in info JSONB) (
    sfunc = replayka.next_visitor_state,
    stype = int[3],
    initcond = '{0,0,0}'
);