package edu.pitt.spotify.rest;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.pitt.spotify.utils.DbUtilities;

/**
 * Servlet implementation class get_songs
 */
@WebServlet("/api/get_songs")
public class get_songs extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public get_songs() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.setContentType("text/html");
		
		response.setContentType("application/json");
		String searchTerm;
		String sql = "";
		JSONObject searchResults = new JSONObject();
		final int RESULTS_LIMIT = 100;

		if(request.getParameter("searchTerm") != null){
			searchTerm = request.getParameter("searchTerm");
			
			if(!searchTerm.equals("")){

				try {
					//***********************************************************
					// Let's deal with songs first
					//***********************************************************
					
					sql = "SELECT * FROM song WHERE title LIKE '" + searchTerm + "%' LIMIT " + RESULTS_LIMIT + ";";
					
					JSONArray songList = new JSONArray();

					DbUtilities db = new DbUtilities();
					ResultSet rs = db.getResultSet(sql);
					while(rs.next()){
						JSONObject song = new JSONObject();
						song.put("song_id", rs.getString("song_id"));
						song.put("title", rs.getString("title"));
						song.put("release_date", rs.getString("release_date"));
						song.put("record_date", rs.getString("record_date"));
						song.put("length", rs.getInt("length"));
						songList.put(song);
					}

					// Store song list in searchResults JSON object
					searchResults.put("songs", songList);

					
					
					//***********************************************************
					// Now let's deal with albums
					//***********************************************************
					
					sql = "SELECT * FROM album WHERE title LIKE '" + searchTerm + "%' LIMIT " + RESULTS_LIMIT + ";";
					
					JSONArray albumList = new JSONArray();
					
					rs = db.getResultSet(sql);
					while(rs.next()){
						JSONObject album = new JSONObject();
						album.put("album_id", rs.getString("album_id"));
						album.put("title", rs.getString("title"));
						album.put("release_date", rs.getTimestamp("release_date"));
						album.put("cover_image_path", rs.getString("cover_image_path"));
						album.put("recording_company_name", rs.getString("recording_company_name"));
						album.put("number_of_tracks", rs.getInt("number_of_tracks"));
						album.put("PMRC_rating", rs.getString("PMRC_rating"));
						album.put("length", rs.getDouble("length"));
						albumList.put(album);
					}

					// Store album list in searchResults JSON object
					searchResults.put("albums", albumList);
					
					
					//***********************************************************
					// Finally, let's deal with artists
					//***********************************************************
					sql = "SELECT * FROM artist "
							+ "WHERE first_name LIKE '" + searchTerm + "%' "
							+ "OR last_name LIKE '" + searchTerm + "%' "
							+ "OR band_name LIKE '" + searchTerm + "%' "
							+ "LIMIT " + RESULTS_LIMIT + ";";
					
					JSONArray artistList = new JSONArray();
					
					rs = db.getResultSet(sql);
					while(rs.next()){
						JSONObject artist = new JSONObject();
						artist.put("artist_id", rs.getString("artist_id"));
						artist.put("first_name", rs.getString("first_name"));
						artist.put("last_name", rs.getString("last_name"));
						artist.put("band_name", rs.getString("band_name"));
						artist.put("bio", rs.getString("bio"));
						
						artistList.put(artist);
					}

					// Store album list in searchResults JSON object
					searchResults.put("artists", artistList);
					
					
					response.getWriter().write(searchResults.toString()); // In this case your sending back a JSON OBJECT and you have to 
																		  // call it differently with a framework. 

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} // end of check for empty searchTerm parameter o
			
			
		}// end of check for null searchTerm parameter	
		
		//***************************OLD CODE TO JUST SEARCH THE FOR SONGS AND ARTISTS USING TWO SEPARATE SERVLETS. IGNORE*******************
		/*
		response.setContentType("application/json");
		// Title
		// Artist
		// Album
		// Year
		String title = "", artist1 = "", album = "", songYear = "";
		String sql = "";
		if(request.getParameter("title") != null){
			title = request.getParameter("title");
			if(!title.equals("")){
				sql = "SELECT * FROM song WHERE title LIKE '%" + title + "%';";
			}
			if(sql.equals("")){
				sql = "SELECT * FROM song;";
			}
		
		
		JSONArray songList = new JSONArray();
		
		try {
			DbUtilities db = new DbUtilities();
			ResultSet rs = db.getResultSet(sql);
			while(rs.next()){
				JSONObject song = new JSONObject();
				song.put("song_id", rs.getString("song_id"));
				song.put("title", rs.getString("title"));
				song.put("release_date", rs.getString("release_date"));
				song.put("record_date", rs.getString("record_date"));
				song.put("length", rs.getInt("length"));
				songList.put(song);
			}
			response.getWriter().write(songList.toString()); // In this Case your sending back a JSON ARRAY and you can iterate through it. 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}
		// response.getWriter().write(sql);
		/*
		JSONArray songList = new JSONArray();
		
		try {
			DbUtilities db = new DbUtilities();
			ResultSet rs = db.getResultSet(sql);
			while(rs.next()){
				JSONObject song = new JSONObject();
				song.put("song_id", rs.getString("song_id"));
				song.put("title", rs.getString("title"));
				song.put("release_date", rs.getString("release_date"));
				song.put("record_date", rs.getString("record_date"));
				song.put("length", rs.getInt("length"));
				songList.put(song);
			}
			response.getWriter().write(songList.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
