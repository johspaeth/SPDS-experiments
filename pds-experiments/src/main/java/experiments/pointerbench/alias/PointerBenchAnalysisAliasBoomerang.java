package experiments.pointerbench.alias;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

import boomerang.Boomerang;
import boomerang.DefaultBoomerangOptions;
import boomerang.Query;
import boomerang.jimple.Statement;
import boomerang.jimple.Val;
import boomerang.results.BackwardBoomerangResults;
import boomerang.BackwardQuery;
import boomerang.ForwardQuery;
import boomerang.seedfactory.SeedFactory;
import experiments.pointerbench.pointsto.PointerBenchBoomerangOptions;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG;
import sync.pds.solver.nodes.Node;
import wpds.impl.Weight.NoWeight;

public class PointerBenchAnalysisAliasBoomerang extends PointerBenchAliasAnalysis {

	public PointerBenchAnalysisAliasBoomerang(String pointerBenchClassesPath, String mainClass) {
		super(pointerBenchClassesPath, mainClass);
	}
	
	private Set<ForwardQuery> getPointsTo(BackwardQuery q){
		Boomerang solver = new Boomerang(new PointerBenchBoomerangOptions()) {
			@Override
			public BiDiInterproceduralCFG<Unit, SootMethod> icfg() {
				return icfg;
			}

			@Override
			public SeedFactory<NoWeight> getSeedFactory() {
				return seedFactory;
			}
		};
		BackwardBoomerangResults<NoWeight> res = solver.solve(q);
		return res.getAllocationSites().keySet();
	}
	
	@Override
	protected boolean computeQuery(AliasQuery q) {
		Set<ForwardQuery> allocsA = getPointsTo(q.queryA);
		Set<ForwardQuery> allocsB = getPointsTo(q.queryB);
		System.out.println(q.queryA);
		System.out.println(allocsA);
		System.out.println(q.queryB);
		System.out.println(allocsB);
		for(ForwardQuery a : allocsA){
			if(allocsB.contains(a))
				return true;
		}
		return false;
	}
}
