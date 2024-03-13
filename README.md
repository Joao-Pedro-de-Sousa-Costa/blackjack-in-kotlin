<h1 align="center">Blackjack In Kotlin 🃏</h1>

<p align="center">
  <img alt="GitHub commit activity" src="https://img.shields.io/github/commit-activity/m/theakarui/blackjack-in-kotlin?style=flat-square">
  <img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/theakarui/blackjack-in-kotlin?style=flat-square">
  <img alt="GitHub issues" src="https://img.shields.io/github/issues/theakarui/blackjack-in-kotlin?style=flat-square">
  <img alt="GitHub language" src="https://img.shields.io/github/languages/top/theakarui/blackjack-in-kotlin?style=flat-square">
  <img alt="GitHub repo size" src="https://img.shields.io/github/repo-size/theakarui/blackjack-in-kotlin?style=flat-square">
</p>

<p align="center">
  <img alt="GitHub stars" src="https://img.shields.io/github/stars/theakarui/blackjack-in-kotlin?style=social">
  <img alt="GitHub forks" src="https://img.shields.io/github/forks/theakarui/blackjack-in-kotlin?style=social">
</p>

## Overview ♠️

This project, developed in Kotlin, aims to implement the classic card game "Blackjack" as part of the academic activities for the Paradigms of Programming Languages course at Universidade Estadual da Paraíba (UEPB).

## Team Members ♥️

- Anniely Medeiros: [@annielymariah](https://github.com/annielymariah)
- Gabriel Soares: [@Gaaaybe](https://github.com/Gaaaybe)
- João Costa: [@TheAkarui](https://github.com/jotapesc)

## Subject ♣️

- **Subject name:** Paradigms
- **Professor:** Janderson Jason Barbosa Aguiar
- **Course:** Computer Science
- **Period:** 2023.2

## Game Specifications ♦️

### Game Type: Twenty-One (Blackjack)

- The game should have a graphical interface, even if simple.
- Text-based interface acceptable only if the language does not easily support a graphical interface.
- Present a Top 5 with at least these details: name, score, and date/time of the match. (Note: scores should be stored, even if in a text file.)
- Scoring method to be defined by the group (e.g., considering the time to win a match).
- Suggestion: explore the possibility of using SQLite.
- Two players (human and "machine").
- The "machine" should make "smart" moves. Purely random gameplay is not acceptable.
- Adapted game rules outlined below...

### Game Rules

- Use 1 deck (without jokers), i.e., 52 cards (13 cards for each of the four suits). Numeric cards have the value of the number on them. Jack (J), Queen (Q), and King (K) are worth 10. The Ace can be worth 1 or 11, with the value that favors the player prevailing (human or machine).
- The goal is to get as close as possible to the number 21 without exceeding it. A player who exceeds the value of 21 loses the game. Only the combination of cards that adds up to 21 points is considered a Blackjack. (Note: Blackjacks should be considered in the definition of the match score).
- There will be several games in a match. Cards from the entire deck should be distributed (shuffled) to the players in the match. A match lasts as long as all players have at least 1 card to play. In a game, a player satisfied with the current score can "stand" (not show a new card), while other players can continue (until everyone stands, or until everyone exceeds 21, or until a player has a Blackjack in the round).
- The human player always plays first.
- The user must define how many machines will be opponents (1, 2, or 3). There must be a "double mode" (two pairs), where a game is won when a pair gets closer to the value of 42 without individually exceeding 21 (in this mode, cards are revealed alternating players as follows: Human-Pair1, Machine1-Pair2, Machine2-Pair1, Machine3-Pair2).
