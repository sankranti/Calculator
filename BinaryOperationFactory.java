public class BinaryOperationFactory
{
    public BinaryOperation makeOperationClass(char operatorType)
    {
        BinaryOperation binOp = null;
        
        if(operatorType == '+')
            return new AddOperation();
        else if(operatorType == '-')
            return new MinusOperation();
        else if(operatorType == '*')
            return new ProductOperation();
        return null;
    }
}