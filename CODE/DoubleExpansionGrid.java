package doris;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 *
 * @author Pier Palamara <pier@cs.columbia.edu>
 */
public class DoubleExpansionGrid extends Grid {

    double currentFrom, currentInterval, currentTo;
    double generation1From, generation1Interval, generation1To;
    double generation2From, generation2Interval, generation2To;
    double ancestral1From, ancestral1Interval, ancestral1To;
    double ancestral2From, ancestral2Interval, ancestral2To;
    boolean doneCurrent = false, doneGeneration1 = false, doneGeneration2 = false,
            doneAncestral1 = false, doneAncestral2 = false;
    double state[] = new double[]{0, 0, 0, 0, -1};

    public DoubleExpansionGrid(String gridParams) throws Exception {
        FileReader fstream = new FileReader(gridParams);
        BufferedReader in = new BufferedReader(fstream);
        String str;
        int counter = 0;
        while (true) {
            str = in.readLine();
            if (str == null) {
                break;
            }
            String[] splitString = str.split("\\s+");
            if (splitString[0].equalsIgnoreCase("current")) {
                currentFrom = Double.parseDouble(splitString[1]);
                currentInterval = Double.parseDouble(splitString[2]);
                currentTo = Double.parseDouble(splitString[3]);
                doneCurrent = true;
            } else if (splitString[0].equalsIgnoreCase("generation1")) {
                generation1From = Double.parseDouble(splitString[1]);
                generation1Interval = Double.parseDouble(splitString[2]);
                generation1To = Double.parseDouble(splitString[3]);
                if (generation1From % 1 != 0 || generation1Interval % 1 != 0 || generation1To % 1 != 0) {
                    throw new Exception("Generations are required to be integer numbers only");
                }
                doneGeneration1 = true;
            } else if (splitString[0].equalsIgnoreCase("generation2")) {
                generation2From = Double.parseDouble(splitString[1]);
                generation2Interval = Double.parseDouble(splitString[2]);
                generation2To = Double.parseDouble(splitString[3]);
                if (generation2From % 1 != 0 || generation2Interval % 1 != 0 || generation2To % 1 != 0) {
                    throw new Exception("Generations are required to be integer numbers only");
                }
                doneGeneration2 = true;
            } else if (splitString[0].equalsIgnoreCase("ancestral1")) {
                ancestral1From = Double.parseDouble(splitString[1]);
                ancestral1Interval = Double.parseDouble(splitString[2]);
                ancestral1To = Double.parseDouble(splitString[3]);
                doneAncestral1 = true;
            } else if (splitString[0].equalsIgnoreCase("ancestral2")) {
                ancestral2From = Double.parseDouble(splitString[1]);
                ancestral2Interval = Double.parseDouble(splitString[2]);
                ancestral2To = Double.parseDouble(splitString[3]);
                doneAncestral2 = true;
            } else {
                throw new Exception("Problem parsing grid file " + gridParams + " line starting with " + splitString[0]
                        + ". Please specify Current, Generation1, Generation2, Ancestral1, Ancestral2.");
            }
        }
        if (!doneCurrent) {
            throw new Exception("Current was not specified in " + gridParams);
        }
        if (!doneGeneration1) {
            throw new Exception("Generation1 was not specified in " + gridParams);
        }
        if (!doneAncestral1) {
            throw new Exception("Ancestral1 was not specified in " + gridParams);
        }
        if (!doneGeneration2) {
            throw new Exception("Generation2 was not specified in " + gridParams);
        }
        if (!doneAncestral2) {
            throw new Exception("Ancestral2 was not specified in " + gridParams);
        }
    }

    void resetIterator() {
        state[0] = 0;
        state[1] = 0;
        state[2] = 0;
        state[3] = 0;
        state[4] = -1;
    }

    boolean hasNext() {
        return !(state[0] == Math.floor((currentTo - currentFrom) / currentInterval)
                && state[1] == Math.floor((generation1To - generation1From) / generation1Interval)
                && state[2] == Math.floor((ancestral1To - ancestral1From) / ancestral1Interval)
                && state[3] == Math.floor((generation2To - generation2From) / generation2Interval)
                && state[4] == Math.floor((ancestral2To - ancestral2From) / ancestral2Interval));
    }

    double[] nextSet() {
//        System.out.println((currentFrom + state[0] * currentInterval) + " " + (generation1From + state[1] * generation1Interval) + " " +
//                (ancestral1From + state[2] * ancestral1Interval) + " " + (generation2From + state[3] * generation2Interval) + " " +
//                (ancestral2From + state[4] * ancestral2Interval));
//        do {
        if (state[4] == Math.floor((ancestral2To - ancestral2From) / ancestral2Interval)) {
            state[4] = 0;
            if (state[3] == Math.floor((generation2To - generation2From) / generation2Interval)) {
                state[3] = 0;
                if (state[2] == Math.floor((ancestral1To - ancestral1From) / ancestral1Interval)) {
                    state[2] = 0;
                    if (state[1] == Math.floor((generation1To - generation1From) / generation1Interval)) {
                        state[1] = 0;
                        if (state[0] == Math.floor((currentTo - currentFrom) / currentInterval)) {
                            state[0] = -1;
                            return null;
                        } else {
                            state[0]++;
                        }
                    } else {
                        state[1]++;
                    }
                } else {
                    state[2]++;
                }
            } else {
                state[3]++;
            }
        } else {
            state[4]++;
        }
//        }
//        while (!(generation1From + state[1] * generation1Interval < generation2From + state[2] * generation2Interval) && hasNext());
//        System.out.println((currentFrom + state[0] * currentInterval) + " " + (generation1From + state[1] * generation1Interval) + " " +
//                (ancestral1From + state[2] * ancestral1Interval) + " " + (generation2From + state[3] * generation2Interval) + " " +
//                (ancestral2From + state[4] * ancestral2Interval));
        double ret[] = new double[5];
        ret[0] = currentFrom + state[0] * currentInterval;
        ret[1] = generation1From + state[1] * generation1Interval;
        ret[2] = ancestral1From + state[2] * ancestral1Interval;
        ret[3] = generation2From + state[3] * generation2Interval;
        ret[4] = ancestral2From + state[4] * ancestral2Interval;
        return ret;
    }
}
