package br.usp.each.saeg.badua;

import org.junit.runner.RunWith;

import br.usp.each.saeg.jaguar.heuristic.impl.JaccardHeuristic;
import br.usp.each.saeg.jaguar.runner.JaguarRunnerHeuristic;
import br.usp.each.saeg.jaguar.runner.JaguarSuite;

@RunWith(JaguarSuite.class)
@JaguarRunnerHeuristic(JaccardHeuristic.class)
public class AllTests {

}
