# game-of-life

An implementation of Conway's Game of Life


## Usage

The program reads input from stdin of the form:
  0 1 0 0 0
  1 0 0 1 1
  1 1 0 0 1
  0 1 0 0 0
  1 0 0 0 1
where 1 is a live cell and 0 is a dead cell. The next generation is printed to
stdout in the same form.


## Example

    lein run < test-input.txt 


## Running Tests

    lein test


## License

Copyright © 2014 Jason Kapp
