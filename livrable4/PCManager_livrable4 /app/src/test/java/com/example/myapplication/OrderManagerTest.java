package com.example.myapplication;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import static java.lang.System.*;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OrderManagerTest {

    @Mock
    private DatabaseReference mockDatabaseReference;

    @Mock
    private DatabaseReference mockChildReference;

    @Mock
    private DatabaseReference mockPushReference;

    private OrderManager orderManager;

    private static boolean matches(Object o) {
        if (o instanceof Order) {
            Order order = (Order) o;
            long currentTime = System.currentTimeMillis();
            return order.getCreatedAt() <= currentTime && order.getModifiedAt() <= currentTime;
        }
        return false;
    }

    @Before
    public void setUp() {
        // Initialiser les mocks
        MockitoAnnotations.openMocks(this);

        // Configurer les comportements simulés de Firebase
        when(mockDatabaseReference.child(anyString())).thenReturn(mockChildReference);
        when(mockDatabaseReference.push()).thenReturn(mockPushReference);
        when(mockPushReference.child(anyString())).thenReturn(mockChildReference);

        // Initialiser OrderManager avec un mock DatabaseReference
        orderManager = new OrderManager(mockDatabaseReference);
    }

    @Test
    public void testAddOrder_Success() {
        // Test 1 : Ajout d'une commande avec succès
        Order order = new Order("order123", "Requester", "order123", currentTimeMillis(), currentTimeMillis(),"pending");

        orderManager.addOrder(order);

        verify(mockPushReference).setValue(order);
    }

    @Test
    public void testAddOrder_NullOrder() {
        // Test 2 : Ajout d'une commande null
        orderManager.addOrder(null);

        verify(mockPushReference, never()).setValue(any());
    }

    @Test
    public void testGetOrderById_Success() {
        // Test 3 : Récupération d'une commande par son ID
        String orderId = "order123";
        ValueEventListener mockListener = mock(ValueEventListener.class);

        orderManager.getOrderById(orderId, mockListener);

        verify(mockDatabaseReference).child(orderId);
        verify(mockChildReference).addListenerForSingleValueEvent(mockListener);
    }

    @Test
    public void testGetOrderById_NullId() {
        // Test 4 : Récupération avec un ID null
        ValueEventListener mockListener = mock(ValueEventListener.class);

        orderManager.getOrderById(null, mockListener);

        verify(mockDatabaseReference, never()).child(anyString());
        verify(mockChildReference, never()).addListenerForSingleValueEvent(any());
    }

    @Test
    public void testDeleteOrder_Success() {
        // Test 5 : Suppression d'une commande par son ID
        String orderId = "order123";

        orderManager.deleteOrder(orderId);

        verify(mockChildReference).removeValue();
    }

    @Test
    public void testDeleteOrder_NullId() {
        // Test 6 : Suppression avec un ID null
        orderManager.deleteOrder(null);

        verify(mockChildReference, never()).removeValue();
    }

    @Test
    public void testUpdateOrderStatus_Success() {
        // Test 7 : Mise à jour du statut d'une commande
        String orderId = "order123";
        String newStatus = "Completed";

        DatabaseReference mockStatusReference = mock(DatabaseReference.class);
        when(mockChildReference.child("status")).thenReturn(mockStatusReference);

        orderManager.updateOrderStatus(orderId, newStatus);

        verify(mockChildReference).child("status");
        verify(mockStatusReference).setValue(newStatus);
    }

    @Test
    public void testUpdateOrderStatus_NullId() {
        // Test 8 : Mise à jour du statut avec un ID null
        orderManager.updateOrderStatus(null, "Completed");

        verify(mockChildReference, never()).child(anyString());
    }

    @Test
    public void testAddOrder_TimestampsSetCorrectly() {
        // Test 9 : Vérifier que les timestamps sont bien définis
        long currentTime = currentTimeMillis();
        Order order = new Order("order123", "Requester", "order123" , currentTime, currentTime, " long");

        orderManager.addOrder(order);

        verify(mockPushReference).setValue(argThat(OrderManagerTest::matches
        ));
    }

    @Test
    public void testAddMultipleOrders_Success() {
        // Test 10 : Ajout de plusieurs commandes
        Order order1 = new Order("order123", "Requester1", "order123", currentTimeMillis(), currentTimeMillis(),"pending");
        Order order2 = new Order("order124", "Requester2", "order123", currentTimeMillis(), currentTimeMillis(),"pending");

        orderManager.addOrder(order1);
        orderManager.addOrder(order2);

        verify(mockPushReference, times(2)).setValue(any(Order.class));
    }
}