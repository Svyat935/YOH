GET_ATTEMPT_PAGINATION = """
select array(
    select
        "id" as "ids" 
    from "started_games" 
    where 
        "game_patient_id" = %(gp_id)s and 
        "date_end" is not null 
    order by "date_end"
) as "attemps";
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
        sum("missclicks") as "missclicks"
    from "game_statistics" 
    where 
        "started_game_id" = %(sg_id)s::uuid
    group by "level", "level_name"
    order by "level"
) a;
"""

"""

-- Виджет кликов
-- $1 - id запущенной игры, $2 - uuid пациента
with check_rights as (
    select EXISTS(select null from "started_game" where "id" = $1 and "patient" = $2)
)
select 
    "level_name",
    sum("clicks") as "clicks",
    sum("missclicks") as "missclicks"
from "game_statistics" 
where 
    (TABLE check_rights)::boolean and
    "sg_id" = $1
group by "level", "level_name"
order by "level";


-- Виджет правильных и неправильных ответов
-- $1 - id запущенной игры, $2 - uuid пациента
with check_rights as (
    select EXISTS(select null from "started_game" where "id" = $1 and "patient" = $2)
)
select 
    "level_name",
    sum(*) filter (where "Type" = 1) as "correct",
    sum(*) filter (where "Type" = 2) as "incorrect"
from "game_statistics" 
where 
    (TABLE check_rights)::boolean and
    "sg_id" = $1
group by "level", "level_name"
order by "level";


-- Виджет игрового времени
-- $1 - id запущенной игры, $2 - uuid пациента
with check_rights as (
    select EXISTS(select null from "started_game" where "id" = $1 and "patient" = $2)
)
select 
    "level_name",
    "date_start",
    "date_end"
from "game_statistics" 
where 
    (TABLE check_rights)::boolean and
    "sg_id" = $1
order by "level", "date_start";


"""