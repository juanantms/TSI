package tracks.singlePlayer.evaluacion.src_MARTINEZ_SANCHEZ_JUAN_ANTONIO;


import java.util.ArrayList;
import java.util.LinkedList;

import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;
import tools.Vector2d;


public class AgenteDFS extends AbstractPlayer{
	
	private Vector2d fescala;
	private Nodo inicial;
	private Nodo objetivo;
	private Vector2d portal;
	private boolean empieza;
	
	private LinkedList<Nodo> ruta;

	 
	public AgenteDFS(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
		
		fescala = new Vector2d(stateObs.getWorldDimension().width / stateObs.getObservationGrid().length,
				stateObs.getWorldDimension().getHeight() / stateObs.getObservationGrid()[0].length);
		
		ArrayList<Observation>[] posiciones = stateObs.getPortalsPositions(stateObs.getAvatarPosition());
		
		portal = posiciones[0].get(0).position;
		portal.x = Math.floor(portal.x / fescala.x);
		portal.y = Math.floor(portal.y / fescala.y);
		empieza = false;
		Vector2d pos_ini = new Vector2d(stateObs.getAvatarPosition().x / 
        		fescala.x, stateObs.getAvatarPosition().y / fescala.y);
        
        inicial = new Nodo(pos_ini);
        objetivo = new Nodo(portal);
 
	}
	
	
	@Override
    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
      
		if(!empieza) {
			empieza = true;
			DFS dfs = new DFS(stateObs);
			
			long tInicio = System.nanoTime();
	        ruta = dfs.Profundidad(inicial, objetivo);
	        long tFin = System.nanoTime();
	        long tiempoTotalenMiliSegundos = (tFin - tInicio)/1000000;
	        
	        System.out.println("Tamaño de la ruta: " + ruta.size());
	        System.out.println("Nodos expandidos: " + dfs.nodosexpandidos);
	        System.out.println("Nodos en memoria: " + dfs.maximoNodos);
	        System.out.println("Tiempo acumulado: " + tiempoTotalenMiliSegundos);
	        
		}
		
		
		Types.ACTIONS a = ruta.pollFirst().accion;
        return a;
	}
}
