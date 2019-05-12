import java.util.ArrayList;

public class AlgorithmLine {

    ArrayList<Integer> Values;

    AlgorithmLine(){
        Values = new ArrayList<>();
    }

    public void AddCurrentItemSet(Integer toAdd){
        if(!Values.contains(toAdd)){
        Values.add(toAdd);
        }
    }

    public void AddNewItemSet(Integer toAdd){
        if(!Values.isEmpty()) {
            Values.add(-1);
        }

        Values.add(toAdd);
    }

    public Boolean IsEmpty(){
        return Values.isEmpty();
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();

        for(Integer value : Values){
            stringBuilder.append(value).append(" ");
        }

        stringBuilder.append(-2);
        return (stringBuilder.toString()).trim();
    }
}
