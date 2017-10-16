
# joptparse
Databind based Command Line parser.

## Example

```java
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
      @ParseInfo
      public List<String> parseInfo;
  }
  
  public static void main(String... args) {
      Args arguments = JoptParse.parse(Args.class, args);
      if (arguments.isParseSucceeded) {
          System.out.println("input: " + arguments.input);
          System.out.println("verbose: " + arguments.verbose);
          System.out.println("operands: " + arugments.operands);
      } else {
          System.out.println("parse error info: " + arguments.parseInfo);
      }
  }
}
```

## TODO
- Better error handling
- Add compatibility tests among traditional libraries(e.g. GNU Getopt)  
