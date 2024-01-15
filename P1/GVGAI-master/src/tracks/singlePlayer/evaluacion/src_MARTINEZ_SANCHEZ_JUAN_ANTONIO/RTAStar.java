package tracks.singlePlayer.evaluacion.src_MARTINEZ_SANCHEZ_JUAN_ANTONIO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;
import tools.Vector2d;

public class RTAStar {
	
ArrayList<Nodo> ruta;
	
	private Comparator<Nodo> comp;
	
	StateObservation state;
	public ArrayList<Observation> grid[][];
	ArrayList<Integer> obstacleItypes;
	
	public int nodosexpandidos;
	public int maximoNodos;
	
	private static int[] x_arrNeig = null;
    private static int[] y_arrNeig = null;
	
	public RTAStar(StateObservation stateObs) {
		this.grid = stateObs.getObservationGrid();
		state = stateObs;
		
		obstacleItypes = new ArrayList<>();       
        obstacleItypes.add(0); // muros
        obstacleItypes.add(4); // trampa
		
		ruta = new ArrayList<>();
		nodosexpandidos = 0;
		maximoNodos = 0;
		
		init();
		
		comp = new Comparator<Nodo>() {
			public int compare(Nodo n1, Nodo n2) {
		    	return n1.compareTo(n2);
		    }
		};
		
	}
	
	private void init()
    {
        if(x_arrNeig == null)
        {
            //TODO: This is a bit of a hack, it wouldn't work with other (new) action sets.
            ArrayList<Types.ACTIONS> actions = this.state.getAvailableActions();
            if(actions.size() == 3)
            {
                //left, right
                x_arrNeig = new int[]{-1, 1};
                y_arrNeig = new int[]{0,  0};
            }else
            {
                //up, down, left, right
                x_arrNeig = new int[]{0,    0,    -1,    1};
                y_arrNeig = new int[]{-1,   1,     0,    0};
            }
        }
    }
	
	public boolean isObstacle(int row, int col)
    {
        if(row<0 || row>=grid.length) return true;
        if(col<0 || col>=grid[row].length) return true;

        for(Observation obs : grid[row][col])
        {
            if(obstacleItypes.contains(obs.itype))
                return true;
        }

        return false;

    }
	
	public ArrayList<Nodo> getNeighbours(Nodo nodo) {
        ArrayList<Nodo> neighbours = new ArrayList<Nodo>();
        int x = (int) (nodo.position.x);
        int y = (int) (nodo.position.y);

        for(int i = 0; i < x_arrNeig.length; ++i)
        {
            if(!isObstacle(x+x_arrNeig[i], y+y_arrNeig[i]))
            {
            	Nodo n = new Nodo(new Vector2d(x+x_arrNeig[i], y+y_arrNeig[i]), nodo);
            	
            	if(nodo.position.x < n.position.x) {
	                n.accion = Types.ACTIONS.ACTION_RIGHT;   
	            }else if(nodo.position.x > n.position.x) {
	            	n.accion = Types.ACTIONS.ACTION_LEFT;
	            }else if(nodo.position.y < n.position.y) {
	            	n.accion = Types.ACTIONS.ACTION_DOWN;
	            }else if(nodo.position.y > n.position.y) {
	            	n.accion =  Types.ACTIONS.ACTION_UP;
	            }

                neighbours.add(n);
            }
        }

        return neighbours;
    }
	
	private ArrayList<Nodo> calcularPath(Nodo nodo) {
		ArrayList<Nodo> path = new ArrayList<>();
		while(nodo != null) {
			if(nodo.parent != null) {
				nodo.setMoveDir(nodo.parent);
				path.add(0, nodo);
			}
			nodo = (Nodo) nodo.parent;
		}
		return path;
	}
	
	
	public double DistanciaManhattan(Nodo current, Nodo objetivo) {
		double Diffx = Math.abs(current.position.x - objetivo.position.x);
        double Diffy = Math.abs(current.position.y - objetivo.position.y);

        return Diffx+Diffy;
	}
	
	public ArrayList<Nodo> RTAstar(Nodo inicial, Nodo objetivo) {
		
      
	}

}
