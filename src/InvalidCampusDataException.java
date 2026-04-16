public class InvalidCampusDataException extends Exception {
    public InvalidCampusDataException(String message) {
        super(message);
    }
}
/*
Ye file ek custom exception define karti hai jo puri app me use hota hai. 
Iska kaam hai invalid inputs (jaise negative fees ya full capacity) ko handle karna taaki program crash na ho.
*/
