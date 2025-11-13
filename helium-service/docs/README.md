# Migrations

Migrating to RosterEntry model requires the following migration commands.

## Create Roster Entries

```
INSERT INTO platinum_roster_entry (player_id, team_registration_id, rostered_date, active)
SELECT preg.player_id as player_id, 
       treg.id as team_registration_id, 
	   TIMESTAMP '2024-10-06 00:00:00' AT TIME ZONE 'EST' as rostered_date,
	   true
  FROM platinum_player_registration preg
  JOIN platinum_team_registration treg on (preg.team_id = treg.team_id and preg.season_id = treg.season_id);
```

## Drop Team Id from Player Registration

```
ALTER TABLE platinum_player_registration
DROP COLUMN team_id;
```