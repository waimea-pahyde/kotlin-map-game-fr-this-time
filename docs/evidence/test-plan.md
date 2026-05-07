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

## Valid - testing that weapon damage is correct

Testing that the weapon damage gets multiplied by the damage multiplier

### Test Data To Use

I will pick up the weapon in map[2], then print out the damage multiplier.

### Expected Test Result

The print equation will display correctly.

---

## Boundry - testing that the random damage stays between 10-50

Testing that the random addition to the damage stays between 10-50 damage

### Test Data To Use

I will print out the random damage element 10 times, and check if it goes in or out of those bounds.

### Expected Test Result

I will get 10 damages between 10 and 50, which could include 10 & 50.

---

## Valid - Enemy takes the correct amount of damage

Testing that the enemy takes the amount of damage it's supposed to

### Test Data To Use

I will print out the amount of damage done, and then cross reference it to the amount of damage the pop up supplies.
I will also check it against the enemys health before and after.

### Expected Test Result

Both the print statement and the popup will show the correct amount of damage.

---

## Boundary - Enemy becomes dead once it's health hits zero

Testing that the status of the enemy becomes 'dead' once it's health hits zero, not the tick after.

### Test Data To Use

I will print out the amount of health the enemy has & it's status for three different enemys.

### Expected Test Result

The print out should never say '0 health' and alive . IT should only show the enemies on low health, then on 0 health
and dead.

---

## Boundary - Enemy becomes dead once it's health hits zero

Testing that the status of the enemy becomes 'dead' once it's health hits zero, not the tick after.

### Test Data To Use

I will print out the amount of health the enemy has & it's status for three different enemys.

### Expected Test Result

The print out should never say '0 health' and alive . IT should only show the enemies on low health, then on 0 health
and dead.

---

## Invalid - player can't move while the enemy is still alive

Testing that the player cannot change rooms while the enemy is still alive

### Test Data To Use

I will try to move rooms while fighting an enemy.

### Expected Test Result

The buttons to move left and right will be disabled.

---

## Valid - clicking through dialogue increments index of current dialogue

Testing that clicking through the dialogue will increase the indexOfCurrentDialogue variable.

### Test Data To Use

I will click through the dialogue while printing out the variable to see if it increases

### Expected Test Result

The number will increase

---

## Boundary - Clicking on the last dialogue sets lastDialogue to true

Testing that when clicking on the last dialogue, it says the variable 'lastDialogue' to true.

### Test Data To Use

I will click through the dialogue while printing out the last dialogue, and when on the lsat dialogue for any given
enemy, it should set it to true

### Expected Test Result

On the last dialogue, the variable will be set to true

---

## Boundary - being on the last title screen sets lasttitlescreen to true

Testing that when clicking on the last title screen, it sets the variable 'lastTitleScreen' to true.

### Test Data To Use

I will click through the title screens while printing out the last titlescreen, and when on the lsat title screen, it
should set it to true

### Expected Test Result

On the last title screen, the variable will be set to true

---

## Valid - testing the game is won when all locations are complete

Testing that the game is won when all the title screens are set to complete.

### Test Data To Use

I will complete all locations, and once I have beaten the last location, see if the win game screen triggers.

### Expected Test Result

Once I kill the last enemy, the win title screen should trigger.

---

## Valid - testing the is lost when the player runs out of health

Testing that the game is lost when the player health hits 0

### Test Data To Use

I will lose when fighting an enemy and make sure the screen triggers.

### Expected Test Result

Once I run out of health, the game over screen will trigger.

---

## Valid - the player can move from one location to the next

Testing that the player can move from one location to the next

### Test Data To Use

I will move from one location to the next and back again.

### Expected Test Result

The room will change when I press the change room arrows.

---

## Valid - the game will show how many seconds it took the player to beat the game

Testing that, once the game is won, the game will show the player how many seconds it took them.

### Test Data To Use

I will beat the game and see if it shows.

### Expected Test Result

The game will show how many seconds it took.

---

## Valid - clicking on the dialogue advances it forward and starts the game on the last one.

Testing that the player can click on the dialogue and it will change, and start the game.

### Test Data To Use

I will click on the dialogue and see if it advances it forward.

### Expected Test Result

The dialogue will cycle through and then start the game.

---