package me.leslie.bfckjvmcompiler;

import me.leslie.bfckjvmcompiler.BrainfuckParser.*;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

public class BfckVisitor extends BrainfuckBaseVisitor<List<String>>{

    int loopcounter;

    public BfckVisitor(){
        super();
        loopcounter = 1;
    }



    private List<String> pushPtr(List<String> target){
        target.add("; push pointer to stack");
        target.add("iload 1");

        return target;
    }

    private List<String> popPtr(List<String> target){
        target.add("; store pointer in var");
        target.add("istore 1");

        return target;
    }





    @Override 
    public List<String> visitIncrease(IncreaseContext ctx){
        List<String> ret = new ArrayList<>();
        
        ret.add("; load pointed val");
        ret.add("aload_0");
        ret.add("iload_1");
        ret.add("iaload");
        ret.add(" ");

        ret.add("; increment");
        ret.add("ldc " + 1);
        ret.add("iadd");
        ret.add("istore_2");
        ret.add(" ");

        ret.add("; writeback");
        ret.add("aload_0");
        ret.add("iload_1");
        ret.add("iload_2");
        ret.add("iastore");




        return ret;
    }
    
    @Override
    public List<String> visitDecrease(DecreaseContext ctx){
        List<String> ret = new ArrayList<>();

        ret.add("; load pointed val");
        ret.add("aload_0");
        ret.add("iload_1");
        ret.add("iaload");
        ret.add(" ");

        ret.add("; increment");
        ret.add("ldc " + 1);
        ret.add("isub");
        ret.add("istore_2");
        ret.add(" ");

        ret.add("; writeback");
        ret.add("aload_0");
        ret.add("iload_1");
        ret.add("iload_2");
        ret.add("iastore");

        return ret;
    }

    @Override 
    public List<String> visitLshift(LshiftContext ctx){
        List<String> ret = new ArrayList<>();

        ret = pushPtr(ret);

        ret.add("; subtract 1 from cell ptr");
        ret.add("ldc 1");
        ret.add("isub");

        ret = popPtr(ret);

        return ret;
    }

    @Override
    public List<String> visitRshift(RshiftContext ctx){
        List<String> ret = new ArrayList<>();

        ret = pushPtr(ret);

        ret.add("; subtract 1 from cell ptr");
        ret.add("ldc 1");
        ret.add("iadd");

        ret = popPtr(ret);

        return ret;
    }

    @Override 
    public List<String> visitLoop(LoopContext ctx){
        List<String> ret = new ArrayList<>();
        String label = new StringBuilder().append("Loop").append(loopcounter++).toString();
        ret.add("; start of loop");
        ret.add(label + ":");
        ret.add(" ");
        ret.addAll(visitChildren(ctx.statements));
        ret.add(" ");
        ret.add("; end of loop");
        ret.add("aload_0");
        ret.add("iload_1");
        ret.add("iaload");
        ret.add("ifne " + label);
        return ret;
    }

    @Override
    public List<String> visitProg(ProgContext ctx){
        return visitChildren(ctx);
    }

    @Override
    public List<String> visitPut(PutContext ctx) {
        List<String> ret = new ArrayList<>();
        ret.add("; print out current val");
        ret.add("getstatic java/lang/System/out Ljava/io/PrintStream;");
        ret.add(" ");

        ret.add("; load pointed val");
        ret.add("aload_0");
        ret.add("iload_1");
        ret.add("iaload");
        ret.add(" ");
        
        ret.add("invokevirtual java/io/PrintStream/print(C)V");

        return ret;
    }

    @Override
    public List<String> visitGet(GetContext ctx) {
        List<String> ret = new ArrayList<>();
        ret.add("; prep cell");
        ret.add("aload_0");
        ret.add("iload_1");
        ret.add("; get 1 char from the console");
        ret.add("getstatic java/lang/System/in Ljava/io/InputStream;");
        ret.add("invokevirtual java/io/InputStream/read()I");
        ret.add("iastore");

        return ret;
    }

    @Override
    protected List<String> defaultResult() {
        return new ArrayList<>();
    }

    @Override
    protected List<String> aggregateResult(List<String> aggregate, List<String> nextResult) {
        if(aggregate == null && nextResult == null){
            return null;
        }

        if(aggregate == null){
            return nextResult;
        }

        if(nextResult == null){
            return aggregate;
        }

        ArrayList<String> ret = new ArrayList<>();

        ret.addAll(aggregate);
        ret.addAll(nextResult);

        return ret;
    }

}