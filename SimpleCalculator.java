import java.util.Stack;
import java.util.HashMap;
import java.util.Scanner;
/*
 * Simple calculator
 * Logic: 
 * Take the infix notation and convert to postfix notation.
 * Feed the postFix notation to evaluate.
 * This class uses a mix of factory pattern to choose the right implementation
 * dynamically and also a strategy pattern/Composition  where the Operation class is used 
 * as a attribute of the class. At runtime, the behavior of this class can change.
 * So in future if you want to add sqrt as a new operator, all you need to do is to
 * add a new implmentation for the square root logic and choose the precdence. 
 * This helps in low coupling and also helps in extending the feature set for the 
 * calculator.
 * Limitations: 
 * Handles only 1 char operand for initial input.
 * Error conditions are not taken care of.
 */
public class SimpleCalculator
{
    String postFixNotation;
    
     private HashMap<Character, Integer> opPrecedence = 
        new HashMap<Character, Integer>();
    
    private BinaryOperation binaryOperation;
    BinaryOperationFactory binOpFactory = new BinaryOperationFactory();
    
    public SimpleCalculator()
    {
        opPrecedence.put('+',1);
        opPrecedence.put('-',1);
        opPrecedence.put('*',2);
    }
    
    public int compute( String expr)
    {
        String postFixExpr = infixToPostfix(expr);
        return calculate(postFixExpr);
    }
    
    
    /*
     * Logic:
     * 1. If the char is a number, push to stack.
     * 2. If the char is operator, create the appropriate impl of the class
     *    using the factory pattern.
     * 3. Send the top 2 values from the stack to the implemented class to do 
     *    the appropriate computation.
     * 4. After the computation push the computed value back to stack.
     *   
     */
    public int calculate(String postFixExpr)
    {
        int finalVal = 0;
        char[] postFixArr = postFixExpr.toCharArray();
        
        Stack<Integer> tempStack = new Stack<Integer>();
        for(int i=0;i<postFixArr.length;i++)
        {
            if(isNumber(postFixArr[i]))
            {
                tempStack.push(Character.getNumericValue(postFixArr[i]));
            }
            else if(isOperator(postFixArr[i]))
            {
                binaryOperation = binOpFactory.makeOperationClass(postFixArr[i]);
                
                int n1 = tempStack.pop();
                int n2 = tempStack.pop();
                
                tempStack.push(binaryOperation.evaluate(n1, n2));
            }
            else //error case. major hack.
                return 0;
        }
        finalVal = tempStack.pop();
        
        return finalVal;
    }
    
    /*
     * Helper method to check if it is a number.
     */
    private boolean isNumber(char c)
    {
        if(c > '0' && c <'9')
            return true;
        return false;
    }
    
    /*
     * Helper method to check if it is valid operator.
     */
    private boolean isOperator(char c)
    {
        return (opPrecedence.containsKey(c));
    }
    
    /*
     * Check element at the top of stack and the current char. 
     * If top of stack > current element, return true;
     */
    private boolean isHigherPrecedence(Stack opStack, char d)
    {
        if(opStack == null || opStack.isEmpty()) return false;
        if(opPrecedence.get(opStack.peek()) >= opPrecedence.get(new Character(d)))
            return true;
        return false;
    }
    
    /*
     * Logic:
     * 1. Maintain a string buffer and a stack.
     * 2. If the char is a number, assign to the buffer.
     * 3. If the char is an operator and the operator on the top is of lower or equal
     *    precedence, push the char to the operator stack.
     * 4. If the char is an operator and the top of the stack has a higher precedence
     *    operator, pop the stack and append to the buffer and push the current char
     *    to the stack.
     * 5. After looping over the array, check if there are elements on the stack.
     *    Pop the remaining operators from the stack and append to the buffer string.
     */
    private String infixToPostfix(String infixExpr)
    {
        char[] inputArray = infixExpr.toCharArray();
       
        StringBuffer outputPostFixStr = new StringBuffer();
        Stack<Character> operatorStack = new Stack<Character>();
        
        for(int i=0;i<inputArray.length;i++)
        {
          
            if(inputArray[i] >='0' && inputArray[i]<='9')
                outputPostFixStr.append(inputArray[i]);
            else if(isOperator(inputArray[i]) 
                     && isHigherPrecedence(operatorStack,inputArray[i]))
            {
                char op = operatorStack.pop();
                operatorStack.push(inputArray[i]);
                outputPostFixStr.append(op);
            }
            else if(isOperator(inputArray[i]))
                operatorStack.push(inputArray[i]);
        }
        
        while(!operatorStack.isEmpty())
        {
            outputPostFixStr.append(operatorStack.pop());
        }
        return outputPostFixStr.toString();
    }
    
    public static void main(String args[])
    {
        //String postFix = "234*+";
        //String postFix = "23+4*";
        //String postFix = "23*4+"; 
        //String infix = "2*4";
        SimpleCalculator simple = new SimpleCalculator();
        
        Scanner console = new Scanner(System.in);
        String inputString = console.nextLine();
        System.out.println("Final:"+simple.compute(inputString));
    }
}


