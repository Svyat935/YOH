GET_ATTEMPT_PAGINATION = """
select array(
    select
        "id" as "ids" 
    from "started_games" 
    where 
        "game_patient_id" = %(gp_id)s and 
        "date_end" is not null 
    order by "date_end"
) as "attempts";
"""

GET_ALL_TIME_WIDGET = """
select 
    array_agg("level_name") as "level_names",
    array_agg("spend_time") as "spend_time",
    (select 
        EXTRACT(epoch FROM ("date_end" - "date_start")) as "spend_time" 
    from "started_games" 
    where "id" = %(sg_id)s::uuid)::int as "all_time_spent"
from (
    select 
        "level_name",
        sum(EXTRACT(epoch FROM ("date_end" - "date_start")))::int as "spend_time"
    from "game_statistics" 
    where 
        "started_game_id" = %(sg_id)s::uuid
    group by "level", "level_name"
    order by "level"
) a;
"""

CLICKS_WIDGET = """
select
    array_agg("level_name") as "level_names",
    array_agg("clicks") as "clicks",
    array_agg("missclicks") as "missclicks"
from (
    select 
        "level_name",
        sum("clicks") as "clicks",
        sum("miss_clicks") as "missclicks"
    from "game_statistics" 
    where 
        "started_game_id" = %(sg_id)s::uuid
    group by "level", "level_name"
    order by "level"
) a;
"""

ANSWERS_WIDGET = """
select
    array_agg("level_name") as "level_names",
    array_agg("correct") as "correct",
    array_agg("incorrect") as "incorrect"
from (
    select 
        "level_name",
        count(*) filter (where "type" = 1) as "correct",
        count(*) filter (where "type" = 2) as "incorrect"
    from "game_statistics" 
    where 
        "started_game_id" = %(sg_id)s::uuid and
        "type" = any(array[1, 2]::int[])
    group by "level", "level_name"
    order by "level"
) a;
"""

TIMELINE_WIDGET = """
select 
    "level_name",
    array["date_start", "date_end"] as "daterange"
from "game_statistics" 
where 
    "started_game_id" = %(sg_id)s::uuid
order by "level", "date_start";

"""
