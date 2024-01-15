package tracks.singlePlayer.evaluacion.src_MARTINEZ_SANCHEZ_JUAN_ANTONIO;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import core.game.Observation;
import core.game.StateObservation;
import ontology.Types;
import tools.Vector2d;

public class DFS {
	
	public static Queue<Nodo> Q;
	LinkedList<Nodo> ruta;
	ArrayList<Nodo> visitados;
	
	StateObservation state;
	public ArrayList<Observation> grid[][];
	ArrayList<Integer> obstacleItypes;
	
	public int nodosexpandidos;
	public int maximoNodos;
	
	private static int[] x_arrNeig = null;
    private static int[] y_arrNeig = null;
	
	
	public DFS(StateObservation stateObs) {
		
		this.grid = stateObs.getObservationGrid();
		state = stateObs;
		
		obstacleItypes = new ArrayList<>();       
        obstacleItypes.add(0); // muros
        obstacleItypes.add(4); // trampa
		
		ruta = new LinkedList<>();
		nodosexpandidos = 0;
		maximoNodos = 0;
		
		init();
		
		visitados = new ArrayList<Nodo>();
		
	}
	
	
	/**
	 * Función extraída de la clase PathFinder.
	 * Inicializa dos arrays para luego generar nodos.
	 */
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
	
	
	/**
	 * Función extraída de la clase PathFinder.
	 * Comprueba en el tablero si hay obstáculo.
	 */
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
	
	
	/**
	 * 
	 * Función extraída de la clase PathFinder y adaptada a la práctica.
	 * Genera los hijos del nodo pasado por parámetro con el orden de hijo Arriba, hijo Abajo, hijo Izquierdo y hijo Derecho.
	 * Devuelve los hijos generados en un ArrayList de nodos inicializados con su posición correspondiente y con 'nodo' como padre.
	 * Asigna el movimiento que se realiza para llegar desde el padre.
	 *
	 * @param nodo del que se generan los hijos.
	 * @return ArrayList<Nodo> 
	 */
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
	
	
	/**
	 * 
	 * Función extraída de la clase PathFinder.
	 * Calcula el camino desde el Nodo nodo hasta el nodo inicial.
	 * 
	 * @param nodo
	 * @return ArrayList<Nodo>
	 */
	private LinkedList<Nodo> calcularPath(Nodo nodo) {
		LinkedList<Nodo> path = new LinkedList<>();
		while(nodo != null) {
			if(nodo.parent != null) {
				nodo.setMoveDir(nodo.parent);
				path.add(0, nodo);
			}
			nodo = (Nodo) nodo.parent;
		}
		return path;
	}
	
	
	
	/**
	 * 
	 * ALGORITMO DE BUSQUEDA DFS:
	 * 
	 * Utilizaremos la función Profundidad_Search() para expandir los nodos.
	 * 
	 * @param inicial: Nodo inicial desde se empiza la búsqueda.
	 * @param objetivo: Nodo objetivo.
	 * @return ArrayList<Nodo> con la ruta hasta el nodo objetivo.
	 */
	public LinkedList<Nodo> Profundidad(Nodo inicial, Nodo objetivo) {
		inicial.parent = null;
		visitados.add(inicial);
		Profundidad_Search(inicial, objetivo);
		return ruta;
	}
	

	/**
	 * 
	 * Función Profundidad_Search()
	 * 
	 * Expande en nodo inicial hasta el objetivo
	 * 
	 * @param inicial: Nodo inicial desde se empiza la búsqueda.
	 * @param objetivo: Nodo objetivo.
	 * @return True si hay camino, False si no.
	 */
	public boolean Profundidad_Search(Nodo inicial, Nodo objetivo) {
		
		// Expandimos el nodo inicial y comprobamos si es objetivo.
		nodosexpandidos++;
		if(inicial.position.equals(objetivo.position)) {
			// Si es objetivo generamos la ruta con la función calcularPath().
			ruta = calcularPath(inicial);
			// Devolvemos true ya que encontramos la ruta hasta el objetivo.
    		return true;
    	}
		
		// Si no es objetivo generaremos los nodos hijos del nodo 'inicial' con la función getNeighbours().
		ArrayList<Nodo> hijos = getNeighbours(inicial);
		boolean sigue = false;
		
		// Recorramos los hijos mientras no encontremos el objetivo.
    	for(int i=0; i<hijos.size() && !sigue; i++) {    
    		// Si el hijo no está en el ArrayList de nodos visitados
    		if(!visitados.contains(hijos.get(i))) {
    			// Lo añadimos
    			visitados.add(hijos.get(i));
    			// Guardamos el tamaño de visitados hasta obtener el máximo de nodos en memoria.
    			maximoNodos = visitados.size();
    			// Expandimos el hijo y repetimos el proceso.
    			sigue = Profundidad_Search(hijos.get(i), objetivo);
    		}
    	}
    	
    	return sigue;
	}
	
	
}
