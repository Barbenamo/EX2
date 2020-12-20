# Object Oriented Programming: assignment 2:
## this assignment was coded, edited and solved by:
### 1.**Bar Ben Amo.**
### 2.**Dror Tapiero.**
### 3.**Chaya Blank.**
#### the assignment has two main parts: the data structure creation, and the Pokemon game design.
Part 1:
=======
the data structure that the assignment is built on is an directional weighted graph.
for info about this type of graph you can visit: [https://en.wikipedia.org/wiki/Directed_graph].
the process of building this data structrue was to implement and program the interfaces sent by Boaz-Ben-Moshe.
here are the classes of this data structure:
| class | description |
| ------|:-----------:|
| nodeData| represents a vertex on the grap |  
| edgeData| represent an edge which connets two different node |
| GeoLocation| represents a 3 coordinates point |
| DWGraph_DS | represents an graph based on the nodes and edges |
| DWGraph_Algo | a data structure based on the DWGraph_DS structrue that offers variuos algorithms |

a display of each class and its variables:
#### ![dataStructureInfo](https://user-images.githubusercontent.com/74831687/102719728-54628f00-42f8-11eb-924a-17a46520431a.png)

algorithms used:
----------------
in order to calculate the shortest path between two given edges on the graph, we used dijkstra's algorithm (DFS).

for info about this specific algorithm please visit: https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm.

the 1st part also contains various methods to serialize and deserialize the data structure to a JSON formated txt file.


Part 2:
=======
the second part of the assignment is to initiate, program and run the "Pokemon Game".
the pokemon game is a game based on the data structure from part 1, which contains agents and pokemons.
the agents are to catch as many pokemons as they can in order to score points whithin the given time.

the 2nd part is about programming the agent to the highest efficiency possible, to achieve the heighest score while making the least 
moves possible.
it is also about editing the GUI (Graph-User Interface) in oreder to display a nice and simple game that the user can understand easily.

the foundation of part 2:
| class | description | 
|-------|:------------:|
|CL_Agent| this class represents an agent on the graph, he can travel the vertices and the edges on different speeds|
|CL_Pokemon| this class represents the pokemon on the graph, he is located on the edges and respawn to a different one when being caught|
|Arena | this class creates and control the game, it initiates the agents and the pokemons on the graph and updates the graph constantly|
|ex2| this class connects between the game server to our classes|
|MyFrame| this class creates the GUI and control the frame of the window that holds the graph|
| MyPanel| this class creates the panel, edits the background and it holds all the physical charactaristics of the window|

Game Strategy:
----------
our strategy was to move the agents according to only DFS algorithm for starters.
then, we upgraded our algorithms to assign each pokemon to one agents, thus, each agent will try to catch different pokemons.


