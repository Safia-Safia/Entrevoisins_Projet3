package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;

import org.hamcrest.collection.IsArrayContaining;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Unit test on Neighbour service
 */
@RunWith(JUnit4.class)
public class NeighbourServiceTest {

    private NeighbourApiService service;

    @Before
    public void setup() {
        service = DI.getNewInstanceApiService();
    }

    @Test
    public void getNeighboursWithSuccess() {
        List<Neighbour> neighbours = service.getNeighbours();
        List<Neighbour> expectedNeighbours = DummyNeighbourGenerator.DUMMY_NEIGHBOURS;
        assertThat(neighbours, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedNeighbours.toArray()));
    }

    @Test
    public void deleteNeighbourWithSuccess() {
        Neighbour neighbourToDelete = service.getNeighbours().get(0);
        service.deleteNeighbour(neighbourToDelete);
        assertFalse(service.getNeighbours().contains(neighbourToDelete));
    }

    @Test
    public void onNeighbourClick(){
        Neighbour neighbourGot = service.getNeighbours().get(0);
        Neighbour neighbourToGet = service.getNeighbours().get(0);
        assertTrue(neighbourToGet.equals(neighbourGot));
    }

    @Test
    public void isOnFavorite(){
        //To check if the neighbour is on the favorite list
        Neighbour neighbourGot =service.getNeighbours().get(0);
        List<Neighbour> favoriteNeighbourList = service.favoriteNeighbour();
        service.addFavoriteNeighbour(neighbourGot); //adding the neighbour 0 to the favorite list
        assertTrue(favoriteNeighbourList.contains(neighbourGot));
    }

    @Test
    public void deleteFromFavorite(){
        //To delete a neighbour from the favorite list
        Neighbour favoriteNeighbourToDelete = service.getNeighbours().get(0);
        List<Neighbour> favoriteNeighbourList = service.favoriteNeighbour();
        service.deleteFavoriteNeighbour(favoriteNeighbourToDelete);
        assertFalse(favoriteNeighbourList.contains(favoriteNeighbourToDelete));
    }
}
