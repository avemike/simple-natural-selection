# Oversimplified natural selection

> **⚠ Warning**
> Project is currently under development

### Documentation

#### Packages

- <b>animals</b>
    - all animal species inheriting from the `Animal`.
- <b>interfaces</b>
- <b>models</b>
    - <b>animal package</b> - parts of animal aggregated inside `Animal` class.
        - `Attributes` - contains all animal general attributes.
        - `Needs` - contains animal attributes and methods related to fulfilling its needs (e.g. `hunger`) and searching
          for goals (e.g. `searchForGoal`).
        - `Movement` - contains animal methods related to movement on the map: in specific direction or position.
        - `Interaction` - contains animal methods related to animal behaviour and interaction with environment.
    - `Animal` - parent of every animal specie.
    - `Plant` - parent of every plant specie.
    - `GraphicalRepresentative` - describes instances that can be rendered.
    - `Representative` - describes material/physical instances on map.
    - `Spatial` - describes instances with position on map.
- <b>plants</b>
    - all plant species inheriting from the `Plant`.
- <b>services</b>
    - `Config` - static class designed for reading `.properties` files inside configs directory.
- <b>terrain</b> - controls map
- <b>ui</b>
- <b>utils</b>

### Ideas for expansion

- [ ] split `size` parameter into two: `render_size` and `interaction_size`.
- [ ] implement water need:
    - create `non-paintable`and durable spots aligned to border of each water field (let's say 10px spacing)
    - these spots would be water resources
- [ ] render instances with higher `coords.y` first

### Design patterns

#### Used

- <b>Factory method</b>: for example `Foxes` and `Rabbits` are being created by static `create` factory method
- <b>Facade</b>: for example `Simulation` class takes advantage of a previously created facade. To make simulation work
  it uses only three methods, so whole complex system is hidden inside it all.
- <b>Singleton</b>: for example `Simulation` class, which is designed to have only one active instance.

#### To implement

- <b>Adapter</b>: just to make `Plant` and `Animal` to collaborate in some scenarios. Instead of writing semi-duplicated
  code Adapter can be used.

### Naming

- classes: UpperCamelCase
- methods: lowerCamelCase
- variables: snake_case
