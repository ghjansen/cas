#CAS - Cellular Automata Simulator
## About
The Cellular Automata Simulator (CAS) is a project of didactic and scientific purpose, based on the publications of Stephen Wolfram and other great autors like Andrew Ilachinski, Harold V. McIntosh, Joel L. Schiff; also inspired by the ideas of Stanislaw Ulam and John von Neumann. See the full reference on page 65 of [the monograph](http://dsc.inf.furb.br/arquivos/tccs/monografias/2016_2_guilherme-humberto-jansen_monografia.pdf).

The goal of this project is to assist the study of cellular automata by providing a simple platform that allows a practical experience of the theoretical concepts. Hopefully, after becoming comforatable with these concepts, one may feel inspired and ready to explore more complex ones.

<table cellpadding="0" cellspacing="0" border="0">
  <tr>
    <td><img src="cas-docs/screenshots/screenshot1.png.png"/></td>
    <td><img src="cas-docs/screenshots/screenshot2.png.png"/></td> 
  </tr>
  <tr>
    <td><img src="cas-docs/screenshots/screenshot3.png.png"/></td>
    <td><img src="cas-docs/screenshots/screenshot4.png.png"/></td> 
  </tr>
</table>

##Download
Download the **[latest release here](http://www)**. You can also see [all realeases here](https://github.com/ghjansen/cas/releases).

Before using CAS, please consider reading **[this page](http://mathworld.wolfram.com/ElementaryCellularAutomaton.html)** from [Wolfram Research](http://www.wolfram.com) for some basics about elementary cellular automaton. If you know brazilian portuguese you can also read the [full monograph here](http://dsc.inf.furb.br/arquivos/tccs/monografias/2016_2_guilherme-humberto-jansen_monografia.pdf).

CAS is distributed under the [GNU Affero General Public Licence v3.0 (AGPLv3)](http://www.gnu.org/licenses/agpl-3.0.txt) and is compatible with Mac, Linux and Windows.

##Structure
CAS is currently organized in 5 main modules: [`cas-core`](/cas-core), [`cas-control`](/cas-control), [`cas-unidimensional`](/cas-unidimensional), [`cas-ui-desktop`](/cas-ui-desktop) and [`cas-docs`](/cas-docs). The table below shows a quick description of each one of the modules:

Module name | Description
------------|------------
[`cas-core`](/cas-core) | Abstract structures that represent the common characteristics of most cellular automata, independent of the amount of dimensions.
[`cas-control`](/cas-control) | Abstract resources for controlling the simulation lifecycle.
[`cas-unidimensional`](/cas-unidimensional) | One-dimensional implementation of the [`cas-core`](/cas-core) and [`cas-control`](/cas-control) modules, responsible for defining specific characteristics and for controlling the simulation of one-dimensional cellular automata.
[`cas-docs`](/cas-docs) | Documentation.

##Core overview
Originally conceived to simulate [elementary cellular automaton](http://mathworld.wolfram.com/ElementaryCellularAutomaton.html), CAS was designed to allow the implementation of most kinds of cellular automata, independent of the amount of cells, dimensions, iterations, rules, etc.

The data model located inside [`cas-core`](/cas-core) abstracts common characteristics of  cellular automata, forming a fundamental structure required to simulate any cellular automaton. The set diagram below shows the composition between the elements of the data model, suppressing the notion of cardinality.

![](cas-docs/diagrams/cas-core-datamodel.png)

For a better understanding of the data model, the image below highlights some of the elements through a [elementary cellular automaton](http://mathworld.wolfram.com/ElementaryCellularAutomaton.html) by using the representation commonly found in the literature. In this picture you can see [`Rule`](cas-core/src/main/java/com/ghjansen/cas/core/ca/Rule.java) **(A)**, [`Transition`](cas-core/src/main/java/com/ghjansen/cas/core/ca/Transition.java) **(B)**, [`Combination`](cas-core/src/main/java/com/ghjansen/cas/core/ca/Combination.java) **( C)**, [`State`](cas-core/src/main/java/com/ghjansen/cas/core/ca/State.java) **(D)**, [`Space`](cas-core/src/main/java/com/ghjansen/cas/core/physics/Space.java) **(E)** and [`Cell`](cas-core/src/main/java/com/ghjansen/cas/core/physics/Cell.java) **(F)**.

![](cas-docs/diagrams/cas-core-datamodel-representation.png)

From all elements of the data model, `Time`, `Space` and `CellularAutomaton` can be considered as the most important ones. More information about each one of these elements is detailed below.

###Time
Time and space are two concepts closely related by the dimensional aspect, that is, the number of dimensions used by the cellular automaton affects not only the cellular space but also the time. This feature is oriented to the sequential nature of the simulator, which updates the space and time in an atomic operation consecutively until it reaches the parametrized limits for the simulation.

To allow time to operate in a dimensional way, it was organized into two approaches: absolute time and relative time. The absolute time is responsible for maintaining the amount of space iterations already processed by the cellular automaton and can be represented by an integer. The relative time informs the next cell to be processed within the current iteration and is represented by a list of integers, where the number of elements in the list is equal to the number of dimensions of the cellular automaton.

Thus, it is correct to state that the Time class is a dynamic counter incremented from its lower limit (zero) acting on each of the dimensions (relative time) until it completely processes a space iteration (absolute time), repeating this cycle until it reaches the limits of time.

Time increases its counters during a simulation in a mathematical ratio equivalent to the production of the limits of the relative counters multiplied by the limit of the absolute counter, minus one. This formula is shown in the image below, where `a` represents the absolute counter,`d` represents the amount of relative counters (or amount of dimensions), `r<sub>i</sub>` represents a relative counter and `lim` is the function that obtains the limit of each counter.

![](cas-docs/formulas/time-increasement.png)

###Space
The `Space` class is a dynamic array responsible for keeping all cells from the initial condition defined for the simulation (`initial` attribute), the history of the iterations already processed (`history` attribute), the last iteration processed (`last`) and the iteration that is being processed (` current` attribute). The `initial`, `last` and ` current` attributes are lists of generic type in order to allow multidimensional structures, whereas the `history` attribute is a list of lists, since `history` stores copies of `current`. The image below shows a representation of the attributes `initial` **(A)**,` history` **(B)**, `last` **( C)** and `current` **(D)** through the computation of elementary rule 110.

![](cas-docs/diagrams/cas-core-space-representation.png)

###CellularAutomaton
The `CellularAutomaton` class is the conceptual representation of the cellular automaton used in the simulation. The logic of this class uses other important classes like `Rule`, ` Transition`, `Combination` and `State`. The algorithm executed by the cellular automaton is:
- **1**: Get the `Combination` from the `Space` for the current `Time`;
- **2**: Obtain the `Transition` from the `Rule`, corresponding to the `Combination` identified in step **1**;
- **3**: Inform the `State` of the new `Cell` to the `Space`, based on the current `Time` and the `Transition` identified in step **2**;
- **4**: Increase `Time`.

The amount of times that the algorithm of the `CellularAutomaton` is executed can be represented by a mathematical ratio similar to `Time`, except that for the `CellularAutomaton` algorithm there is no subtraction of 1, as shown by the image below.

![](cas-docs/formulas/rule-execution.png)

##Contribute
There is a lot to be improved and created. Check [the list of issues](https://github.com/ghjansen/cas/issues) to see what's happening, maybe including your suggestions or bugs found.

If you feel inspired by one of the issues or by the project itself, feel free to make contact through ![](cas-docs/text/contact.png) .








