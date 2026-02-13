import React, { createContext, useContext, useState, useCallback, useEffect } from 'react';
import { apiService } from '../services/apiService';

/** * AuthContext provides authentication state and functions to manage user login/logout.
 * It initializes the auth state from localStorage and updates it based on API responses.
 * The context includes the current user, authentication status, and functions to log in and out.
 */

// Define the shape of the user object and the context value
interface AuthUser {
  id: string;
  name: string;
  email: string;
  role: 'USER' | 'ADMIN';
}

// Define the shape of the context value
interface AuthContextType {
  isAuthenticated: boolean;
  user: AuthUser | null;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
  token: string | null;
}
// Define the props for the AuthProvider component

interface AuthProviderProps {
  children: React.ReactNode;
}

// Create the AuthContext with an initial value of null
const AuthContext = createContext<AuthContextType | null>(null);

// AuthProvider component that wraps the application and provides authentication state and functions
export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [user, setUser] = useState<AuthUser | null>(null);
  const [token, setToken] = useState<string | null>(localStorage.getItem('token'));

  // Initialize auth state from token
  useEffect(() => {
    const initializeAuth = async () => {
      const storedToken = localStorage.getItem('accesstoken');
      const refreshToken = localStorage.getItem('refreshToken');
      if (storedToken) {
        try {
          // Validate token and fetch user profile
          const userProfile = await apiService.getUserProfile();
          setUser(userProfile);
          setIsAuthenticated(true);
          setToken(storedToken);

          
        } catch (error) {
          // If token is invalid, clear authentication
          localStorage.removeItem('accesstoken');
          localStorage.removeItem('refreshToken');
          setIsAuthenticated(false);
          setUser(null);
          setToken(null);
        }
      }
    };

    initializeAuth();
  }, []);



// Function to handle user login
  const login = useCallback(async (email: string, password: string) => {
    try {
      const response = await apiService.login({ email, password });
      
      localStorage.setItem('accesstoken', response.token);
      localStorage.setItem('refreshToken', response.refreshToken);
      setToken(response.token);
      setIsAuthenticated(true);

      // TODO: Add endpoint to fetch user profile after login
       const  userProfile = await apiService.getUserProfile();
       setUser(userProfile);
    } catch (error) {
      throw error;
    }
  }, []);

  // Function to handle user logout
  const logout = useCallback(() => {
    localStorage.removeItem('accesstoken');
    localStorage.removeItem('refreshToken');
    setIsAuthenticated(false);
    setUser(null);
    setToken(null);
    apiService.logout(); // This clears the token from apiService
  }, []);

 // Update axios instance with the token whenever it changes
  useEffect(() => {
    if (token) {
      // Update axios instance configuration
      const axiosInstance = apiService['api'];
      axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    }
  }, [token]);
// Define the value to be provided by the context
  const value = {
    isAuthenticated,
    user,
    login,
    logout,
    token
  };
// Render the AuthContext provider with the defined value
  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};
// Custom hook to use the AuthContext
export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export default AuthContext;