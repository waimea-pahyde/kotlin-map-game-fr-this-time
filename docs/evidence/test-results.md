# Results of Testing

The test results show the actual outcome of the testing, following the [Test Plan](test-plan.md)

---

## Boundary - Testing the players ability to travel to the boundries of the map

Testing the boundary on the left side (Jerry's forest), and the right side (Jerry's cabin)

### Test Data Used

Jerry's cabin; the first location at map[0], and Jerry's forest at map[4].

### Test Result

![canBeInRoom.png](screenshots/canBeInRoom.png)

![can be in last room](screenshots/canBeInLastRoom.png)

The player can be in both the first and final rooms.

---

## Valid - Enemys showing up in the correct rooms

When enemies show up in the room they're supposed to.

### Test Data Used

I will use room 2, woman-mans fields and woman man to test this.

### Test Result

![Woman man in their room](screenshots/womanManInTheirRoom.png)

Woman-man shows up in room 4, where she is supposed to.

---

## Invalid - Player cannot travel outside the map

Making sure the player can't travel to the location before the first one.

### Test Data Used

I will try to travel right from map[0]

### Test Result

![Can't Go That Way!](screenshots/cantGoThatWay.png)

The program informs the user they cannot go that way

---

## Invalid & Valid - user cannot travel until the weapon has been picked up

When the enemy is killed, the user cannot travel until they have picked up the weapon the enemy dropped

### Test Data Used

I will try to move on from map[0] while the weapon hasn't been picked up, then pick up the weapon and the travel buttons

### Test Result

![Can't Go That Way!](screenshots/cantGoThatWay.png)

The program informs the user they cannot go that way

---


