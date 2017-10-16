
# joptparse
Databind based Command Line parser.

## Example

```java
import net.saka1.joptparse.JoptParse;

public class Demo {
  public static class Args {
      @Option(name = "i", longName = "input")
      public String input;
      @Option(name = "v")
      public boolean verbose;
      @Operands
      public List<String> operands;
      @ParseSucceeded
      public boolean isParseSucceeded;
      @FaildReason
      public List<String> reason;
  }
  
  public static void main(String... args) {
      Args arguments = JOptParse.parse(Args.class, args);
      if (arguments.isParseSucceeded) {
          System.out.println("input: " + arguments.input);
          System.out.println("verbose: " + arguments.verbose);
          System.out.println("operands: " + arugments.operands);
      } else {
          System.out.println("parse error info: " + arguments.reason);
      }
  }
}
```

## TODO
- Better error handling
- Add compatibility tests among traditional libraries(e.g. GNU Getopt)  
- Parse list arguments

