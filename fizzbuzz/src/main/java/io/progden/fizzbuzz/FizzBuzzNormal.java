package io.progden.fizzbuzz;

public class FizzBuzzNormal implements FizzBuzz {
    @Override
    public String fizzBuzz(int i) {
        var output = new StringBuilder();
        if (i % 3 == 0 || i % 5 == 0) {
            if (i % 3 == 0)
                output.append("Fizz");
            if (i % 5 == 0)
                output.append("Buzz");
        } else {
            output.append(i);
        }
        return output.toString();
    }
}
