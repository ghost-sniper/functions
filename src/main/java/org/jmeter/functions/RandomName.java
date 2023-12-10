package org.jmeter.functions;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Get Random Chinese Name
 */
public class RandomName extends AbstractFunction {

    private static final String key = "__RandomName";

    private static final String lastNameFileName = "lastname.txt";

    private static final String firstNameFileName = "lastname.txt";
    private String gender;
    //自定义函数参数说明
    private static final List<String> args = new LinkedList<>();

    static {
        args.add("性别(0-女,1-男)");
    }

    //函数主体
    @Override
    public String execute(SampleResult sampleResult, Sampler sampler) throws InvalidVariableException {
        String lastName = null;
        String firstName = null;
        ClassLoader classLoader = RandomName.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(lastNameFileName)) {
            if (inputStream != null) {
                String s = inputStream.toString();
                String[] lastNameListArray = s.split(",");
                int index = (int) (Math.random() * lastNameListArray.length);
                lastName = lastNameListArray[index];
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (InputStream inputStream = classLoader.getResourceAsStream(firstNameFileName)) {
            if (inputStream != null) {
                String[] array = new BufferedReader(new InputStreamReader(inputStream)).lines().toArray(String[]::new);
                if (Objects.equals(gender, "0")) {
                    String[] firstNameListArray = array[0].split(",");
                    int index = (int) (Math.random() * firstNameListArray.length);
                    firstName = firstNameListArray[index];
                } else {
                    String[] firstNameListArray = array[1].split(",");
                    int index = (int) (Math.random() * firstNameListArray.length);
                    firstName = firstNameListArray[index];
                }
                return firstName;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lastName + firstName;
    }

    //接收用户传入参数
    @Override
    public void setParameters(Collection<CompoundVariable> collection) throws InvalidVariableException {
        Object[] values = collection.toArray();
        gender = ((CompoundVariable) values[0]).execute();
        if (!Objects.equals(gender, "0") && !Objects.equals(gender, "1")) {
            throw new InvalidVariableException("错误的性别参数");
        }
    }

    @Override
    public String getReferenceKey() {
        return key;
    }

    @Override
    public List<String> getArgumentDesc() {
        return args;
    }
}
