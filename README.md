# Oversimplified natural selection

> **⚠ Warning**
> Project is currently under development

### Documentation

#### Packages

- <b>animals</b>
- <b>interfaces</b>
- <b>models</b>
    - <b>animal</b>
- <b>plants</b>
- <b>services</b>
- <b>terrain</b>
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

#### To implement

- <b>Adapter</b>: just to make `Plant` and `Animal` to collaborate in some scenarios. Instead of writing semi-duplicated
  code Adapter can be used.
-

### Naming

- classes: UpperCamelCase
- methods: lowerCamelCase
- variables: snake_case
