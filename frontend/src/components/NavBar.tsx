import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useTheme } from '../components/ThemeContext';
import { Sun, Moon } from 'lucide-react'; // optional icons for toggle
import {
  Navbar,
  NavbarBrand,
  NavbarContent,
  NavbarItem,
  NavbarMenu,
  NavbarMenuItem,
  NavbarMenuToggle,
  Button,
  Avatar,
  Dropdown,
  DropdownTrigger,
  DropdownMenu,
  DropdownItem,
} from '@nextui-org/react';
import { Plus, BookOpen, Edit3, LogOut, User, BookDashed } from 'lucide-react';

/** NavBar component that displays navigation links, user profile, and theme toggle. */


interface NavBarProps {
  isAuthenticated: boolean;
  userProfile?: {
    name: string;
    avatar?: string;
  };
  onLogout: () => void;
}

// NavBar component definition

const NavBar: React.FC<NavBarProps> = ({
  isAuthenticated,
  userProfile,
  onLogout,
}) => {
  const location = useLocation();
  const [isMenuOpen, setIsMenuOpen] = React.useState(false);

  const { theme, toggleTheme } = useTheme();
// Define the main menu items for the navigation bar
  const menuItems = [
    { name: 'Home', path: '/' },
    { name: 'Categories', path: '/categories' },
    { name: 'Tags', path: '/tags' },
  ];
// Helper function to get user initials for avatar fallback
  const getInitials = (name?: string) => {
  if (!name) return "";

  const parts = name.trim().split(" ").filter(Boolean);

  if (parts.length === 1) {
    return parts[0].charAt(0).toUpperCase();
  }

  return (
    parts[0].charAt(0).toUpperCase() +
    parts[parts.length - 1].charAt(0).toUpperCase()
  );
};

// Render the navigation bar with conditional links and user profile
  return (
    <Navbar
      isBordered
      isMenuOpen={isMenuOpen}
      onMenuOpenChange={setIsMenuOpen}
      className="mb-6 bg-gray-200 dark:bg-gray-800"
    >
      
      <NavbarContent className="sm:hidden" justify="start">
        <NavbarMenuToggle />
      </NavbarContent>

      <NavbarContent className="sm:hidden pr-3" justify="center">
        <NavbarBrand>
          <Link to="/" className="font-bold text-inherit">Blog Platform</Link>
        </NavbarBrand>
      </NavbarContent>

      <NavbarContent className="flex items-center gap-4" justify="start">
        <NavbarBrand className="flex items-center gap-2">
          <Link to="/" className="font-bold text-inherit">
            Khan's Blog Platform
          </Link>

          <Button
            size="sm"
            variant="flat"
            color="primary"
            onPress={toggleTheme}
            className="px-2 py-1 min-w-0"
            startContent={theme === 'light' ? <Moon size={16} /> : <Sun size={16} />}
          >
            {theme === 'light' ? 'Dark' : 'Light'}
          </Button>
        </NavbarBrand>
        {menuItems.map((item) => (
          <NavbarItem
            key={item.path}
            isActive={location.pathname === item.path}
          >
            <Link
              to={item.path}
              className={`text-sm ${
                location.pathname === item.path
                  ? 'text-primary'
                  : 'text-default-600'
              }`}
            >
              {item.name}
            </Link>
          </NavbarItem>
        ))}
      </NavbarContent>

      <NavbarContent justify="end">
        {isAuthenticated ? (
          <>
            <NavbarItem>
              <Button
                as={Link}
                to="/posts/drafts"
                color="secondary"
                variant="flat"
                startContent={<BookDashed size={16} />}
              >
                Draft Posts
              </Button>
            </NavbarItem>
            <NavbarItem>
              <Button
                as={Link}
                to="/posts/new"
                color="primary"
                variant="flat"
                startContent={<Plus size={16} />}
              >
                New Post
              </Button>
            </NavbarItem>

            


            <NavbarItem>
              <Dropdown placement="bottom-end">
                <DropdownTrigger>
                  <Avatar
                    isBordered
                    as="button"
                    className="transition-transform"
                    src={userProfile?.avatar}
                    name={getInitials(userProfile?.name)}
                  />
                </DropdownTrigger>
                <DropdownMenu aria-label="User menu">                
                  <DropdownItem
                    key="drafts"
                    startContent={<Edit3 size={16} />}
                  >
                    <Link to="/posts/drafts">My Drafts</Link>
                  </DropdownItem>
                  <DropdownItem
                    key="logout"
                    startContent={<LogOut size={16} />}
                    className="text-danger"
                    color="danger"
                    onPress={onLogout}
                  >
                    Log Out
                  </DropdownItem>
                </DropdownMenu>
              </Dropdown>
            </NavbarItem>
          </>
        ) : (
          <>
            <NavbarItem>
              <Button as={Link} to="/login" variant="flat">
                Log In
              </Button>
            </NavbarItem>

           


          </>
        )}
      </NavbarContent>

      <NavbarMenu>
        {menuItems.map((item) => (
          <NavbarMenuItem key={item.path}>
            <Link
              to={item.path}
              className={`w-full ${
                location.pathname === item.path
                  ? 'text-primary'
                  : 'text-default-600'
              }`}
              onClick={() => setIsMenuOpen(false)}
            >
              {item.name}
            </Link>
          </NavbarMenuItem>
        ))}
      </NavbarMenu>
    </Navbar>
  );
};

export default NavBar;