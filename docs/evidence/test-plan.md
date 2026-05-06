# Plan for Testing the Program

The test plan lays out the actions and data I will use to test the functionality of my program.

Terminology:

- **VALID** data values are those that the program expects
- **BOUNDARY** data values are at the limits of the valid range
- **INVALID** data values are those that the program should reject

---

## Boundary - Testing the players ability to travel to the boundries of the map

Testing the boundary on the left side (Jerry's forest), and the right side (Jerry's cabin)

![map](screenshots/map.png)

### Test Data To Use

Jerry's cabin, the first location at map[0] and Jerry's forest at map[4]

### Expected Test Result

The player can be in both the first and last rooms on the map

---

## Valid - Enemies showing up in the correct room

When enemies show up in the room they're supposed to.

### Test Data To Use

I will use room 2, woman-mans fields and woman man to test this.

### Expected Test Result

Woman man will show up in the eggplant fields, not in the room before or after.

---

## Invalid - Player cannot travel outside the map

Making sure the player can't travel to the location before the first one

### Test Data To Use

I will try to travel right from map[0]

### Expected Test Result

The program will inform the user that they are unable to go that way.

---

## Invalid & Valid - user cannot travel until the weapon has been picked up

When the enemy is killed, the user cannot travel until they have picked up the weapon the enemy dropped

### Test Data To Use

I will try to move on from map[0] while the weapon hasn't been picked up, then pick up the weapon and the travel buttons
should enable.

### Expected Test Result

The left and right arrows disable until the weapon is picked up.

---

