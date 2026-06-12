# Internal hook executed by /inqdel stop after the persistent state is dormant.
scoreboard objectives add inqdel_state dummy
scoreboard players set !inquisition_delayed inqdel_state 0
bossbar set inqui:rednight visible false
