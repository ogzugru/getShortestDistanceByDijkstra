import org.apache.commons.lang3.time.StopWatch;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;


public class Driver {


    public static void main(String[] args) throws IOException {

        // Edge weighted directed graph is created from input

        EdgeWeightedDigraph DAG = new EdgeWeightedDigraph(args[0]);

        GetShortestDistance(DAG, args[1], args[2]);

        StopWatch newStopWatch = new StopWatch();

        newStopWatch.start();


        newStopWatch.stop();

    }

    private static void GetShortestDistance(EdgeWeightedDigraph dag, String arg, String arg1) {

        String result = "";

        int destination = dag.getCitID(arg1);
        int source = dag.getCitID(arg);

        ArrayList<Integer> visitedCities = new ArrayList<Integer>();

        DijkstraSP sp = new DijkstraSP(dag, source);

        if (sp.hasPathTo(destination)) {
            //System.out.printf("%d to %d (%.2f)  ", source, destination, sp.distTo(destination));
            for (DirectedEdge e : sp.pathTo(destination)) {
                visitedCities.add(e.from());
            }

            visitedCities.add(destination);


            Collections.sort(visitedCities);

            result += "2 cities to be visited:";

            for (int x : visitedCities
                    ) {
                result += "\n" + dag.getCitName(x);


            }

            result += "\n" + "Distance: " + (int) sp.distTo(destination) + " km";
            System.out.println(result);
        } else {
            System.out.printf("%d to %d    no path\n", source, destination);
        }


    }

}
