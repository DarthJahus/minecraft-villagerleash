# Villager Leash

Server-side mod for Minecraft that allows players to use lead on villagers and wandering traders, with optional costs.

## Config (`config/villagerleash.json`)
```json
{
  "enable": true,
  "damage_per_second": 0.25,
  "break_chance_per_second": 0.02,
  "anger_on_leash": true,
  "renew_anger": true,
  "anger_renew_interval_seconds": 10
}
```

| Key                            | Default                          | Meaning                                                                       |
|--------------------------------|----------------------------------|-------------------------------------------------------------------------------|
| `enable`                       | `true`                           | Master switch; `false`: vanilla (not leashable)                               |
| `damage_per_second`            | `0.25`<br/>(~80 s to kill 20 HP) | Damage each second while leashed; `0` = off                                   |
| `break_chance_per_second`      | `0.02`                           | Chance each second the lead breaks and drops; `0` = off                       |
| `anger_on_leash`               | `true`                           | On attach, villager treats the holder as who last hurt them (golems / memory) |
| `renew_anger`                  | `true`                           | Re-apply the anger while still leashed                                        |
| `anger_renew_interval_seconds` | `10`                             | Seconds between anger renewals                                                |

Once unleashed (break, player, death, etc.), the mod stops applying any effect to that villager.

## License

Apache-2.0
