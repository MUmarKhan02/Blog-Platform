import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useTheme } from '../components/ThemeContext';
import { Sun, Moon } from 'lucide-react';
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
import { Plus, Edit3, LogOut, BookDashed, User } from 'lucide-react';

interface NavBarProps {
  isAuthenticated: boolean;
  userProfile?: {
    name: string;
    avatar?: string;
  };
  onLogout: () => void;
}

const NavBar: React.FC<NavBarProps> = ({
  isAuthenticated,
  userProfile,
  onLogout,
}) => {
  const location = useLocation();
  const [isMenuOpen, setIsMenuOpen] = React.useState(false);
  const { theme, toggleTheme } = useTheme();

  const menuItems = [
    { name: 'Home', path: '/' },
    { name: 'Categories', path: '/categories' },
    { name: 'Tags', path: '/tags' },
  ];

  const getInitials = (name?: string) => {
    if (!name) return '';
    const parts = name.trim().split(' ').filter(Boolean);
    if (parts.length === 1) return parts[0].charAt(0).toUpperCase();
    return parts[0].charAt(0).toUpperCase() + parts[parts.length - 1].charAt(0).toUpperCase();
  };

  return (
    <Navbar
      isBordered
      isMenuOpen={isMenuOpen}
      onMenuOpenChange={setIsMenuOpen}
      maxWidth="full"
      className="mb-6 bg-gray-200 dark:bg-gray-800 px-4"
    >
      {/* Mobile toggle */}
      <NavbarContent className="sm:hidden" justify="start">
        <NavbarMenuToggle />
      </NavbarContent>

      {/* Mobile brand */}
      <NavbarContent className="sm:hidden pr-3" justify="center">
        <NavbarBrand>
          <Link to="/" className="flex items-center gap-2 font-bold text-inherit">
            <img
              src="/favicon.png"
              alt="Khan's Blog Logo"
              className="w-7 h-7 rounded-full object-cover object-top"
            />
            Blog Platform
          </Link>
        </NavbarBrand>
      </NavbarContent>

      {/* Desktop left — brand + theme toggle + nav links */}
      <NavbarContent className="hidden sm:flex items-center gap-2" justify="start">
        {/* Brand */}
        <NavbarBrand className="flex items-center gap-2 mr-2 flex-shrink-0">
          <Link to="/" className="flex items-center gap-2 font-bold text-inherit whitespace-nowrap">
            <img
              src="/favicon.png"
              alt="Khan's Blog Logo"
              className="w-8 h-8 rounded-full object-cover object-top"
            />
            Khan's Blog Platform
          </Link>
          <Button
            size="sm"
            variant="flat"
            color="primary"
            onPress={toggleTheme}
            className="px-2 py-1 min-w-0 ml-1"
            startContent={theme === 'light' ? <Moon size={16} /> : <Sun size={16} />}
          >
            {theme === 'light' ? 'Dark' : 'Light'}
          </Button>
        </NavbarBrand>

        {/* Nav links */}
        {menuItems.map((item) => (
          <NavbarItem key={item.path} isActive={location.pathname === item.path}>
            <Link
              to={item.path}
              className={`text-sm px-3 py-1.5 rounded-md transition-colors whitespace-nowrap ${
                location.pathname === item.path
                  ? 'text-primary bg-primary/10'
                  : 'text-default-600 hover:text-primary hover:bg-primary/10'
              }`}
            >
              {item.name}
            </Link>
          </NavbarItem>
        ))}
      </NavbarContent>

      {/* Desktop right */}
      <NavbarContent justify="end" className="hidden sm:flex gap-2">
        {isAuthenticated ? (
          <>
            <NavbarItem>
              <Button
                as={Link}
                to="/posts/drafts"
                color="secondary"
                variant="flat"
                size="sm"
                startContent={<BookDashed size={16} />}
              >
                Drafts
              </Button>
            </NavbarItem>
            <NavbarItem>
              <Button
                as={Link}
                to="/posts/new"
                color="primary"
                variant="flat"
                size="sm"
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
                  <DropdownItem key="profile" startContent={<User size={16} />}>
                    <Link to="/profile">My Profile</Link>
                  </DropdownItem>
                  <DropdownItem key="drafts" startContent={<Edit3 size={16} />}>
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
          <NavbarItem>
            <Button as={Link} to="/login" variant="flat" size="sm">
              Log In
            </Button>
          </NavbarItem>
        )}
      </NavbarContent>

      {/* Mobile menu */}
      <NavbarMenu>
        {menuItems.map((item) => (
          <NavbarMenuItem key={item.path}>
            <Link
              to={item.path}
              className={`w-full ${
                location.pathname === item.path ? 'text-primary' : 'text-default-600'
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