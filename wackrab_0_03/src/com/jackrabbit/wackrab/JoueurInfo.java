package com.jackrabbit.wackrab;

import com.jackrabbit.wackrab.sqlite.DbDataPlayer;
import com.jackrabbit.wackrab.utils.Log;

public class JoueurInfo { //is not serialisable

    private String nomJoueur;
    private double x;
    private double y;
    private double z;
    private String nomMonde;

	/*
	public String ToStringFile() {
		return ">>" 
				+ nomJoueur + ";" 
				+ Double.toString(x) + ";" 
				+ Double.toString(y) + ";" 
				+ Double.toString(z) + ";" 
				+ nomMonde ;
	}
	
	public void FromStringFile(String dataFile) {
		String[] data = dataFile.split(";");
		nomJoueur = data[0];
		x =  Double.parseDouble(data[1]);
		y =  Double.parseDouble(data[2]);
		z =  Double.parseDouble(data[3]);
		nomMonde = data[4];
	}*/
	
    /*
	Main main;
	public JoueurInfo(Main main) {
		this.main = main;
	}
	*/

	public void SelectDB(Main main, String _nomJoueur) {

		DbDataPlayer d = main.DTB.players.select(main.DTB, main.pathDB_SQLite, _nomJoueur);
		
		if(d == null) {			
			d = main.DTB.players.newDbDataPlayer(nomJoueur, main.defaultAllowedSubLevelForPlayers,
				(float)x, (float)y, (float)z, nomMonde);
		}
		
		nomJoueur = d.name;
		x = d.lastX;
		y = d.lastY;
		z = d.lastZ;
		nomMonde = d.lastWorld;
		
	}
	
	public void InsertDB(Main main, String _nomJoueur) {
		
		if(nomJoueur == null) {

			if(_nomJoueur == null) {
				return;
			}
			
			nomJoueur = _nomJoueur;
		}
		

		DbDataPlayer d = main.DTB.players.select(main.DTB, main.pathDB_SQLite, nomJoueur);

		
		if(d == null)
			d = main.DTB.players.newDbDataPlayer(nomJoueur, main.defaultAllowedSubLevelForPlayers,
					(float)x, (float)y, (float)z, nomMonde);


		d.lastX = (float)x;
		d.lastY = (float)y;
		d.lastZ = (float)z;

		d.lastWorld = nomMonde;
		d.name = nomJoueur;

		main.DTB.players.insert(main.DTB, main.pathDB_SQLite, d, true);

	}
	
	public JoueurInfo() {}
	
    public JoueurInfo(String nomJoueur, double x, double y, double z, String nomMonde) {
        this.nomJoueur = nomJoueur;
        this.x = x;
        this.y = y;
        this.z = z;
        this.nomMonde = nomMonde;
    }
    

    // Getters
    public String getNomJoueur() {
        return nomJoueur;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public String getNomMonde() {
        return nomMonde;
    }

    // Setters
    public void setNomJoueur(String nomJoueur) {
        this.nomJoueur = nomJoueur;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setNomMonde(String nomMonde) {
        this.nomMonde = nomMonde;
    }
}