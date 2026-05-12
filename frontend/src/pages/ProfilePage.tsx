import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import {
  Card,
  CardBody,
  CardHeader,
  Avatar,
  Chip,
  Divider,
  Button,
} from '@nextui-org/react';
import { Calendar, Mail, FileText, BookDashed, Edit3 } from 'lucide-react';
import { useAuth } from '../components/AuthContext';
import { apiService, Post } from '../services/apiService';

const ProfilePage: React.FC = () => {
  const { user } = useAuth();
  const [posts, setPosts] = useState<Post[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchPosts = async () => {
      try {
        const response = await apiService.getPosts({});
        // Filter to only this user's posts
        const userPosts = response.filter(post => post.author?.id === user?.id);
        setPosts(userPosts);
      } catch (err) {
        // silently fail
      } finally {
        setLoading(false);
      }
    };

    if (user) fetchPosts();
  }, [user]);

  const getInitials = (name?: string) => {
    if (!name) return '';
    const parts = name.trim().split(' ').filter(Boolean);
    if (parts.length === 1) return parts[0].charAt(0).toUpperCase();
    return parts[0].charAt(0).toUpperCase() + parts[parts.length - 1].charAt(0).toUpperCase();
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    });
  };

  const publishedPosts = posts.filter(p => p.status === 'PUBLISHED');

  return (
    <div className="max-w-4xl mx-auto px-4 space-y-6">

      {/* Profile Card */}
      <Card className="w-full">
        <CardBody className="flex flex-col sm:flex-row items-center sm:items-start gap-6 p-6">
          <Avatar
            name={getInitials(user?.name)}
            className="w-24 h-24 text-2xl flex-shrink-0"
            isBordered
            color="primary"
          />
          <div className="flex-1 text-center sm:text-left space-y-3">
            <div>
              <h1 className="text-2xl font-bold">{user?.name}</h1>
              <div className="flex items-center justify-center sm:justify-start gap-1 text-default-500 mt-1">
                <Mail size={14} />
                <span className="text-sm">{user?.email}</span>
              </div>
            </div>

            <div className="flex flex-wrap gap-2 justify-center sm:justify-start">
              <Chip
                color={user?.role === 'ADMIN' ? 'danger' : 'primary'}
                variant="flat"
                size="sm"
              >
                {user?.role === 'ADMIN' ? 'Admin' : 'Member'}
              </Chip>
              <Chip variant="flat" size="sm" startContent={<FileText size={12} />}>
                {publishedPosts.length} {publishedPosts.length === 1 ? 'Post' : 'Posts'}
              </Chip>
            </div>
          </div>

          <div className="flex gap-2 flex-shrink-0">
            <Button
              as={Link}
              to="/posts/new"
              color="primary"
              variant="flat"
              size="sm"
              startContent={<Edit3 size={16} />}
            >
              New Post
            </Button>
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
          </div>
        </CardBody>
      </Card>

      {/* Posts */}
      <Card className="w-full">
        <CardHeader className="px-6 pt-6 pb-2">
          <h2 className="text-xl font-bold">Published Posts</h2>
        </CardHeader>
        <Divider />
        <CardBody className="p-4 space-y-3">
          {loading ? (
            <div className="space-y-3">
              {[...Array(3)].map((_, i) => (
                <div key={i} className="h-16 bg-default-100 rounded-lg animate-pulse" />
              ))}
            </div>
          ) : publishedPosts.length === 0 ? (
            <div className="flex flex-col items-center justify-center py-12 text-center">
              <FileText size={40} className="text-gray-400 mb-3" />
              <p className="text-gray-500 dark:text-gray-400">No published posts yet.</p>
              <Button
                as={Link}
                to="/posts/new"
                color="primary"
                variant="flat"
                size="sm"
                className="mt-3"
              >
                Write your first post
              </Button>
            </div>
          ) : (
            publishedPosts.map((post) => (
              <Link key={post.id} to={`/posts/${post.id}`}>
                <div className="flex items-start justify-between p-3 rounded-lg hover:bg-default-100 transition-colors cursor-pointer">
                  <div className="flex-1 min-w-0">
                    <h3 className="font-semibold text-default-900 truncate">{post.title}</h3>
                    <div className="flex items-center gap-3 mt-1 flex-wrap">
                      <div className="flex items-center gap-1 text-xs text-default-500">
                        <Calendar size={12} />
                        {formatDate(post.createdAt)}
                      </div>
                      <Chip size="sm" color="primary" variant="flat" className="text-xs">
                        {post.category.name}
                      </Chip>
                    </div>
                  </div>
                  <Edit3 size={16} className="text-default-400 flex-shrink-0 mt-1 ml-3" />
                </div>
              </Link>
            ))
          )}
        </CardBody>
      </Card>
    </div>
  );
};

export default ProfilePage;