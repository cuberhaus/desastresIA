package IA.Electrica;

import java.io.FileOutputStream;
import java.io.*;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;
//import java.util.Iterator;
//import java.util.LinkedList;



import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
/**
 * 
 * @author tucan
 *
 * Datos, importar desde archivo
 * treure chivatos per fer jp
 * trobar el limit per el heapjava
 * 
 */

public class jProbes {

	public static void main(String[] args) {
		int c, n;
		
		/*Crearproblemas(15,35,70,5);
		Crearproblemas(15,7,15,2);
		Crearproblemas(60,5,10,2);
			*/
		
		Crearproblemas2(10,4,8,5);
		Crearproblemas2(10,6,12,5);
		Crearproblemas2(15,7,15,5);
		Crearproblemas2(15,30,60,5);
		Crearproblemas2(20,4,8,5);
		Crearproblemas2(20,10,20,5);
		Crearproblemas2(20,30,60,5);
		Crearproblemas2(40,7,15,5);
		Crearproblemas2(40,20,40,5);
		Crearproblemas2(40,30,60,5);
		Crearproblemas2(70,10,20,5);
		Crearproblemas2(70,35,70,5);
		/*	
			// PROBES SA
			//2000, 100, 5, 0.001
			//1000,100,5,0.001
			//2000,1000,5,0.001
			//2000,100,2,0.001
			//2000,100,5,0.1
			//solini=1,3,4  ,3 casos n,c, 3 n,c iguals  3 jogs params
			int si;int p1,p2,p3; double p4;
			//n consumidors, c companyies
			n=30;c=4;
			Electrica e= new Electrica(c,n);
			si=1;
			p1=2000;p2= 100; p3=5;p4=0.001;
			ParamsSA(e, si,1, 2, p1,  p2,  p3, p4);
			p1=1000;p2=100;p3=5;p4=0.001;
			ParamsSA(e, si,1, 2, p1,  p2,  p3, p4);
			p1=2000;p2=1000;p3=5;p4=0.001;
			ParamsSA(e, si,1, 2, p1,  p2,  p3, p4);
			p1=2000;p2=100;p3=2;p4=0.001;
			ParamsSA(e, si,1, 2, p1,  p2,  p3, p4);
			p1=2000;p2=100;p3=5;p4=0.1;
			ParamsSA(e, si,1, 2, p1,  p2,  p3, p4);
			System.out.println(" primer bloc sa");
			si=3;
			p1=2000;p2= 100; p3=5;p4=0.001;
			ParamsSA(e, si,1, 2, p1,  p2,  p3, p4);
			p1=1000;p2=100;p3=5;p4=0.001;
			ParamsSA(e, si,1, 2, p1,  p2,  p3, p4);
			p1=2000;p2=1000;p3=5;p4=0.001;
			ParamsSA(e, si,1, 2, p1,  p2,  p3, p4);
			p1=2000;p2=100;p3=2;p4=0.001;
			ParamsSA(e, si,1, 2, p1,  p2,  p3, p4);
			p1=2000;p2=100;p3=5;p4=0.1;
			ParamsSA(e, si,1, 2, p1,  p2,  p3, p4);
			System.out.println(" segon bloc sa");
			si=4;
			p1=2000;p2= 100; p3=5;p4=0.001;
			ParamsSA(e, si,1, 2, p1,  p2,  p3, p4);
			p1=1000;p2=100;p3=5;p4=0.001;
			ParamsSA(e, si,1, 2, p1,  p2,  p3, p4);
			p1=2000;p2=1000;p3=5;p4=0.001;
			ParamsSA(e, si,1, 2, p1,  p2,  p3, p4);
			p1=2000;p2=100;p3=2;p4=0.001;
			ParamsSA(e, si,1, 2, p1,  p2,  p3, p4);
			p1=2000;p2=100;p3=5;p4=0.1;
			ParamsSA(e, si,1, 2, p1,  p2,  p3, p4);
			System.out.println(" tercer bloc sa");
			
			
*/
			
			
		
			
		
	}
//	c cons, n companyies
	public static void Crearproblemas2(int c, int n,int nump,int marge) {

		int solini, heuristic, operador;
		Electrica e = new Electrica(c, n);
		int np=e.obteNpaquets();
		int ii=0;
		while (!( np<=(nump+marge) && np>=(nump-marge) ) && ii<1000){
			 e = new Electrica(c, n);
			 np=e.obteNpaquets();
			 
			ii++;
			System.out.print(" it="+ii);
		}
			
			System.out.println("trobat p="+np+" it="+ii);
			
			
		
		
				
					solini = 3;
					heuristic = 1;
			
					ExecutarProblema2(e,c, n, solini, heuristic, 2);
					
					solini = 3;
					heuristic = 2;
				
					ExecutarProblema2(e,c, n, solini, heuristic, 2);
					
				
				
			

		
		

		
	}
//c cons, n companyies
	public static void Crearproblemas(int c, int n,int nump,int marge) {

		int solini, heuristic, operador;
		Electrica e = new Electrica(c, n);
		int np=e.obteNpaquets();
		int ii=0;
		while (!( np<=(nump+marge) && np>=(nump-marge) ) && ii<1000){
			 e = new Electrica(c, n);
			 np=e.obteNpaquets();
			 
			ii++;
			System.out.print(" it="+ii);
		}
			
			System.out.println("trobat p="+np+" it="+ii);
			
			
		for (int i = 1; i <= 4; i++) {
			for (int j = 1; j <= 2; j++) {
				
					solini = i;
					heuristic = j;
				if (solini == 1){
				//HC sempre donara el mateix pero SA no pertant fem 3 vegades SA
					ExecutarProblema(e,c, n, solini, heuristic, 2);
					ExecutarProblema(e,c, n, solini, heuristic, 3);
				}else {
					ExecutarProblema(e,c, n, solini, heuristic, 1);
					ExecutarProblema(e,c, n, solini, heuristic, 2);
					ExecutarProblema(e,c, n, solini, heuristic, 3);
				}
				
			}

		}
		for (int i = 1; i <= 2; i++) {
			for (int j = 3; j <= 4; j++) {
				
					solini = i;
					heuristic = j;
				if (solini == 1){
				//HC sempre donara el mateix pero SA no pertant fem 3 vegades SA
					ExecutarProblema(e,c, n, solini, heuristic, 2);
					ExecutarProblema(e,c, n, solini, heuristic, 3);
				}else {
					ExecutarProblema(e,c, n, solini, heuristic, 1);
					ExecutarProblema(e,c, n, solini, heuristic, 2);
					ExecutarProblema(e,c, n, solini, heuristic, 3);
				}
				
			}

		}
	}

	public static void ExecutarProblema(Electrica e,int c, int n, int solinivar,
			int heur, int op) {

		int heurIni, passos, heurFi;
		long temps;
		aima.search.framework.SuccessorFunction sF = null;
		aima.search.framework.HeuristicFunction hF = null;

		Search search = null;
		Problem problem = null;
		

		if (solinivar == 1)
			// buida
			e.solucioInicial1();
		else if (solinivar == 2)
			// tal cual aleatoria
			e.solucioInicial2();
		else if (solinivar == 3) {
			// System.out.println("solucio inicial voraç pitjor");
			if (heur == 1)
				e.solucioInicial3();
			if (heur == 2)
				e.solucioInicial4p();
		} else if (solinivar == 4) {
			// System.out.println("solucio inicial voraç millor");
			if (heur == 1)
				e.solucioInicial4();
			if (heur == 2)
				e.solucioInicial3p();
		}

		switch (op) {
		case 1:
			sF = new ElectricaSuccessorFunction2();
			break;
		case 2:
			sF = new ElectricaSuccessorFunction3();
			break;
		case 3:
			sF = new ElectricaSuccessorFunction4();
			break;

		}
		switch (heur) {
		case 1:
			hF = new ElectricaHeuristicFunction();
			break;
		case 2:
			hF = new ElectricaHeuristicFunction2();
			break;
		case 3:
			hF = new ElectricaHeuristicFunction3();
			break;
		case 4:
			hF = new ElectricaHeuristicFunction4();
			break;
		//case 5:
			//hF = new ElectricaHeuristicFunction5();
			//break;
		}

		problem = new Problem(e, sF, new ElectricaGoalTest(), hF);

		// HillClimbing!
		heurIni = -e.obteValHeuristic(heur);
		search = new HillClimbingSearch();
		try {
			// AGAFAR DADES
			// val heuristic, numc, nump,numcons, tipussolini,
			// int heurIni,temps,passos,heurFi;
			Date d1, d2;
			Calendar a, b;
			d1 = new Date();
			SearchAgent agent = new SearchAgent(problem, search);
			d2 = new Date();

			a = Calendar.getInstance();
			b = Calendar.getInstance();
			a.setTime(d1);
			b.setTime(d2);

			long m = b.getTimeInMillis() - a.getTimeInMillis();
temps=m;
			Electrica ef = (Electrica) search.getGoalState();
			// agent.getActions());
			// AGAFAR DADES
			// val heuristic, temps, passos(posat a la creadora de copia Electrica
			// ESCRIURE DADES A FITXER
			EscriureProblema("./expHC.txt",e, ef,temps,ef.NombreDePassos(),solinivar,heur,op,1);


		} catch (Exception eX) {
			eX.printStackTrace();
		}
		//SimulatedAnnealing!!
		problem = new Problem(e, sF, new ElectricaGoalTest(), hF);
		search = new SimulatedAnnealingSearch(2000, 100, 5, 0.001);
		try {
			// AGAFAR DADES
			// val heuristic, numc, nump,numcons, tipussolini,
			// int heurIni,temps,passos,heurFi;
			Date d1, d2;
			Calendar a, b;
			d1 = new Date();
			SearchAgent agent = new SearchAgent(problem, search);
			d2 = new Date();

			a = Calendar.getInstance();
			b = Calendar.getInstance();
			a.setTime(d1);
			b.setTime(d2);

			long m = b.getTimeInMillis() - a.getTimeInMillis();
temps=m;
			Electrica ef = (Electrica) search.getGoalState();
			// agent.getActions());
			// AGAFAR DADES
			// val heuristic, temps, passos(posat a la creadora de copia Electrica
			// ESCRIURE DADES A FITXER
			EscriureProblema("./expSA.txt",e, ef,temps,ef.NombreDePassos(),solinivar,heur,op,2);


		} catch (Exception eX) {
			eX.printStackTrace();
		}
//		SimulatedAnnealing!!
		problem = new Problem(e, sF, new ElectricaGoalTest(), hF);
		search = new SimulatedAnnealingSearch(2000, 100, 5, 0.001);
		try {
			// AGAFAR DADES
			// val heuristic, numc, nump,numcons, tipussolini,
			// int heurIni,temps,passos,heurFi;
			Date d1, d2;
			Calendar a, b;
			d1 = new Date();
			SearchAgent agent = new SearchAgent(problem, search);
			d2 = new Date();

			a = Calendar.getInstance();
			b = Calendar.getInstance();
			a.setTime(d1);
			b.setTime(d2);

			long m = b.getTimeInMillis() - a.getTimeInMillis();
temps=m;
			Electrica ef = (Electrica) search.getGoalState();
			// agent.getActions());
			// AGAFAR DADES
			// val heuristic, temps, passos(posat a la creadora de copia Electrica
			// ESCRIURE DADES A FITXER
			EscriureProblema("./expSA.txt",e, ef,temps,ef.NombreDePassos(),solinivar,heur,op,2);


		} catch (Exception eX) {
			eX.printStackTrace();
		}
//		SimulatedAnnealing!!
		problem = new Problem(e, sF, new ElectricaGoalTest(), hF);
		search = new SimulatedAnnealingSearch(2000, 100, 5, 0.001);
		try {
			// AGAFAR DADES
			// val heuristic, numc, nump,numcons, tipussolini,
			// int heurIni,temps,passos,heurFi;
			Date d1, d2;
			Calendar a, b;
			d1 = new Date();
			SearchAgent agent = new SearchAgent(problem, search);
			d2 = new Date();

			a = Calendar.getInstance();
			b = Calendar.getInstance();
			a.setTime(d1);
			b.setTime(d2);

			long m = b.getTimeInMillis() - a.getTimeInMillis();
temps=m;
			Electrica ef = (Electrica) search.getGoalState();
			// agent.getActions());
			// AGAFAR DADES
			// val heuristic, temps, passos(posat a la creadora de copia Electrica
			// ESCRIURE DADES A FITXER
			EscriureProblema("./expSA.txt",e, ef,temps,ef.NombreDePassos(),solinivar,heur,op,2);


		} catch (Exception eX) {
			eX.printStackTrace();
		}

	}

	public static void ExecutarProblema2(Electrica e,int c, int n, int solinivar,
			int heur, int op) {

		int heurIni, passos, heurFi;
		long temps;
		aima.search.framework.SuccessorFunction sF = null;
		aima.search.framework.HeuristicFunction hF = null;

		Search search = null;
		Problem problem = null;
		

		if (solinivar == 1)
			// buida
			e.solucioInicial1();
		else if (solinivar == 2)
			// tal cual aleatoria
			e.solucioInicial2();
		else if (solinivar == 3) {
			// System.out.println("solucio inicial voraç pitjor");
			if (heur == 1)
				e.solucioInicial3();
			if (heur == 2)
				e.solucioInicial4p();
		} else if (solinivar == 4) {
			// System.out.println("solucio inicial voraç millor");
			if (heur == 1)
				e.solucioInicial4();
			if (heur == 2)
				e.solucioInicial3p();
		}

		switch (op) {
		case 1:
			sF = new ElectricaSuccessorFunction2();
			break;
		case 2:
			sF = new ElectricaSuccessorFunction3();
			break;
		case 3:
			sF = new ElectricaSuccessorFunction4();
			break;

		}
		switch (heur) {
		case 1:
			hF = new ElectricaHeuristicFunction();
			break;
		case 2:
			hF = new ElectricaHeuristicFunction2();
			break;
		case 3:
			hF = new ElectricaHeuristicFunction3();
			break;
		case 4:
			hF = new ElectricaHeuristicFunction4();
			break;
		//case 5:
			//hF = new ElectricaHeuristicFunction5();
			//break;
		}

		problem = new Problem(e, sF, new ElectricaGoalTest(), hF);

		// HillClimbing!
		heurIni = -e.obteValHeuristic(heur);
		search = new HillClimbingSearch();
		try {
			// AGAFAR DADES
			// val heuristic, numc, nump,numcons, tipussolini,
			// int heurIni,temps,passos,heurFi;
			Date d1, d2;
			Calendar a, b;
			d1 = new Date();
			SearchAgent agent = new SearchAgent(problem, search);
			d2 = new Date();

			a = Calendar.getInstance();
			b = Calendar.getInstance();
			a.setTime(d1);
			b.setTime(d2);

			long m = b.getTimeInMillis() - a.getTimeInMillis();
temps=m;
			Electrica ef = (Electrica) search.getGoalState();
			// agent.getActions());
			// AGAFAR DADES
			// val heuristic, temps, passos(posat a la creadora de copia Electrica
			// ESCRIURE DADES A FITXER
			EscriureProblema("./expProg.txt",e, ef,temps,ef.NombreDePassos(),solinivar,heur,op,1);


		} catch (Exception eX) {
			eX.printStackTrace();
		}
		//SimulatedAnnealing!!
		problem = new Problem(e, sF, new ElectricaGoalTest(), hF);
		search = new SimulatedAnnealingSearch(2000, 100, 5, 0.001);
		try {
			// AGAFAR DADES
			// val heuristic, numc, nump,numcons, tipussolini,
			// int heurIni,temps,passos,heurFi;
			Date d1, d2;
			Calendar a, b;
			d1 = new Date();
			SearchAgent agent = new SearchAgent(problem, search);
			d2 = new Date();

			a = Calendar.getInstance();
			b = Calendar.getInstance();
			a.setTime(d1);
			b.setTime(d2);

			long m = b.getTimeInMillis() - a.getTimeInMillis();
temps=m;
			Electrica ef = (Electrica) search.getGoalState();
			// agent.getActions());
			// AGAFAR DADES
			// val heuristic, temps, passos(posat a la creadora de copia Electrica
			// ESCRIURE DADES A FITXER
			EscriureProblema("./expProg.txt",e, ef,temps,ef.NombreDePassos(),solinivar,heur,op,2);


		} catch (Exception eX) {
			eX.printStackTrace();
		}



	}

	public static void ParamsSA(Electrica e, int solinivar,
			int heur, int op,int p1, int p2, int p3,
			double p4) {

		int heurIni, passos, heurFi;
		long temps;
		aima.search.framework.SuccessorFunction sF = null;
		aima.search.framework.HeuristicFunction hF = null;

		Search search = null;
		Problem problem = null;
		
		if (solinivar == 1)
			// buida
			e.solucioInicial1();
		else if (solinivar == 2)
			// tal cual aleatoria
			e.solucioInicial2();
		else if (solinivar == 3) {
			// System.out.println("solucio inicial voraç pitjor");
			if (heur == 1)
				e.solucioInicial3();
			if (heur == 2)
				e.solucioInicial4p();
		} else if (solinivar == 4) {
			// System.out.println("solucio inicial voraç millor");
			if (heur == 1)
				e.solucioInicial4();
			if (heur == 2)
				e.solucioInicial3p();
		}

		switch (op) {
		case 1:
			sF = new ElectricaSuccessorFunction2();
			break;
		case 2:
			sF = new ElectricaSuccessorFunction3();
			break;
		case 3:
			sF = new ElectricaSuccessorFunction4();
			break;

		}
		switch (heur) {
		case 1:
			hF = new ElectricaHeuristicFunction();
			break;
		case 2:
			hF = new ElectricaHeuristicFunction2();
			break;
		case 3:
			hF = new ElectricaHeuristicFunction3();
			break;
		case 4:
			hF = new ElectricaHeuristicFunction4();
			break;
		//case 5:
			//hF = new ElectricaHeuristicFunction5();
			//break;
		}
		heurIni = -e.obteValHeuristic(heur);
		
		
		//SimulatedAnnealing!!
		problem = new Problem(e, sF, new ElectricaGoalTest(), hF);
		search = new SimulatedAnnealingSearch(p1, p2, p3, p4);
		try {
			
			Date d1, d2;
			Calendar a, b;
			d1 = new Date();
			SearchAgent agent = new SearchAgent(problem, search);
			d2 = new Date();

			a = Calendar.getInstance();
			b = Calendar.getInstance();
			a.setTime(d1);
			b.setTime(d2);

			long m = b.getTimeInMillis() - a.getTimeInMillis();
			temps=m;
			Electrica ef = (Electrica) search.getGoalState();
			EscriureProblema("./SAparams.txt",e, ef,temps,ef.NombreDePassos(),solinivar,heur,op,2);
		} catch (Exception eX) {
			eX.printStackTrace();
		}
//		SimulatedAnnealing!!
		problem = new Problem(e, sF, new ElectricaGoalTest(), hF);
		search = new SimulatedAnnealingSearch(p1, p2, p3, p4);
		try {
			
			Date d1, d2;
			Calendar a, b;
			d1 = new Date();
			SearchAgent agent = new SearchAgent(problem, search);
			d2 = new Date();

			a = Calendar.getInstance();
			b = Calendar.getInstance();
			a.setTime(d1);
			b.setTime(d2);

			long m = b.getTimeInMillis() - a.getTimeInMillis();
			temps=m;
			Electrica ef = (Electrica) search.getGoalState();
			EscriureProblema("./SAparams.txt",e, ef,temps,ef.NombreDePassos(),solinivar,heur,op,2);
		} catch (Exception eX) {
			eX.printStackTrace();
		}
//		SimulatedAnnealing!!
		problem = new Problem(e, sF, new ElectricaGoalTest(), hF);
		search = new SimulatedAnnealingSearch(p1, p2, p3, p4);
		try {
			
			Date d1, d2;
			Calendar a, b;
			d1 = new Date();
			SearchAgent agent = new SearchAgent(problem, search);
			d2 = new Date();

			a = Calendar.getInstance();
			b = Calendar.getInstance();
			a.setTime(d1);
			b.setTime(d2);

			long m = b.getTimeInMillis() - a.getTimeInMillis();
			temps=m;
			Electrica ef = (Electrica) search.getGoalState();
			EscriureProblema("./SAparams.txt",e, ef,temps,ef.NombreDePassos(),solinivar,heur,op,2);
		} catch (Exception eX) {
			eX.printStackTrace();
		}
	}
	public static void EscriureProblema(String ruta,Electrica eini, Electrica efi,long temps,int passos, int si,int heur,int op,int alg) {
	
	
		try {
		
			BufferedWriter bw;PrintWriter salida;
			bw = new BufferedWriter(new FileWriter(ruta, true));
			salida = new PrintWriter(bw);
			salida.println(alg+" "+si+" "+heur+" "+op+" "+temps+" "+passos+"  "+eini.obteNcomp()+" "+eini.obteNpaquets()+" "+eini.obteNdemandes()+" "+eini.obteValHeuristic(heur)+" "+eini.obteBenefici()+" "+eini.obteFactorMig()+" "+eini.obtePagament()+" "+eini.obteQnoAssignada()+" "+eini.obteQnoConsumida()+" "+efi.obteValHeuristic(heur)+" "+efi.obteBenefici()+" "+efi.obteFactorMig()+" "+efi.obtePagament()+" "+efi.obteQnoAssignada()+" "+efi.obteQnoConsumida());
		
			salida.close();
			
		}
		catch(java.io.IOException ioex) { }
		
		
	}


}
