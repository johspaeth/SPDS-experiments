package com.ibm.safe.intraproc.sccp;

import java.io.FileWriter;
import java.util.Iterator;

import com.ibm.safe.intraproc.sccp.ExpandedControlFlowGraph.SingleInstructionBasicBlock;
import com.ibm.wala.ssa.SSACFG;
import com.ibm.wala.ssa.SSACFG.BasicBlock;

/**
 * @author Eran Yahav (yahave)
 * 
 */
public class ExpandedCFGDotWriter {

  public static void write(String fileName, ExpandedControlFlowGraph cfg) throws IllegalArgumentException {
    if (cfg == null) {
      throw new IllegalArgumentException("cfg cannot be null");
    }
    StringBuffer result = new StringBuffer();
    result.append(dotOutput(cfg));
    try {
      FileWriter fw = new FileWriter(fileName, false);
      fw.write(result.toString());
      fw.close();
    } catch (Exception e) {
      throw new RuntimeException("Error writing dot file");
    }
  }

  public static void write(String fileName, SSACFG cfg) {
    if (cfg == null) {
      throw new IllegalArgumentException("cfg cannot be null");
    }
    StringBuffer result = new StringBuffer();
    result.append(dotOutput(cfg));
    try {
      FileWriter fw = new FileWriter(fileName, false);
      fw.write(result.toString());
      fw.close();
    } catch (Exception e) {
      throw new RuntimeException("Error writing dot file");
    }
  }

  private static StringBuffer dotOutput(ExpandedControlFlowGraph cfg) {
    StringBuffer result = new StringBuffer("digraph \"ExpandedControlFlowGraph:\" {\n");
    result.append("center=true;fontsize=12;node [fontsize=12];edge [fontsize=12]; \n");

    // create nodes for basic-blocks
    for (Iterator it = cfg.iterator(); it.hasNext();) {

      SingleInstructionBasicBlock bb = (SingleInstructionBasicBlock) it.next();
      result.append(dotOutput(bb));
      if (bb.isEntryBlock()) {
        result.append(" [color=green]\n");
      } else if (bb.isExitBlock()) {
        result.append(" [color=red]\n");
      } else {
        result.append("\n");
      }

    }
    // create edges
    for (Iterator it = cfg.iterator(); it.hasNext();) {
      SingleInstructionBasicBlock bb = (SingleInstructionBasicBlock) it.next();
      for (Iterator succIt = cfg.getSuccNodes(bb); succIt.hasNext();) {
        SingleInstructionBasicBlock succ = (SingleInstructionBasicBlock) succIt.next();
        result.append(dotOutput(bb));
        result.append(" -> ");
        result.append(dotOutput(succ));

        result.append("\n");
      }
    }
    // close digraph
    result.append("}");
    return result;
  }

  private static StringBuffer dotOutput(SSACFG cfg) {
    StringBuffer result = new StringBuffer("digraph \"ControlFlowGraph:\" {\n");
    result.append("center=true;fontsize=12;node [fontsize=12];edge [fontsize=12]; \n");

    // create nodes for basic-blocks
    for (Iterator it = cfg.iterator(); it.hasNext();) {

      BasicBlock bb = (BasicBlock) it.next();
      result.append(dotOutput(bb));
      if (bb.isEntryBlock()) {
        result.append(" [color=green]\n");
      } else if (bb.isExitBlock()) {
        result.append(" [color=red]\n");
      } else {
        result.append("\n");
      }

    }
    // create edges
    for (Iterator it = cfg.iterator(); it.hasNext();) {
      BasicBlock bb = (BasicBlock) it.next();
      for (Iterator succIt = cfg.getSuccNodes(bb); succIt.hasNext();) {
        BasicBlock succ = (BasicBlock) succIt.next();
        result.append(dotOutput(bb));
        result.append(" -> ");
        result.append(dotOutput(succ));
        result.append("\n");
      }
    }
    // close digraph
    result.append("}");
    return result;
  }

  private static StringBuffer dotOutput(BasicBlock bb) {
    StringBuffer result = new StringBuffer();
    result.append("\"");
    result.append(bb.getNumber());
    result.append("\"");
    return result;
  }

  private static StringBuffer dotOutput(SingleInstructionBasicBlock bb) {
    StringBuffer result = new StringBuffer();
    result.append("\"");
    result.append(bb.getNumber());
    result.append("-");
    result.append(bb.getInstruction());
    result.append("\"");
    return result;
  }

}