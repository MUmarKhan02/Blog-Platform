import React, { useEffect, useState } from 'react';
import { Card, CardBody, Input } from '@nextui-org/react';
import { Search } from 'lucide-react';
import { apiService, Post, Category, Tag } from '../services/apiService';
import PostList from '../components/PostList';
import HeroSection from '../components/HeroSection';

const HomePage: React.FC = () => {
  const [posts, setPosts] = useState<Post[] | null>(null);
  const [categories, setCategories] = useState<Category[]>([]);
  const [tags, setTags] = useState<Tag[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [page, setPage] = useState(1);
  const [sortBy, setSortBy] = useState("createdAt,desc");
  const [selectedCategory, setSelectedCategory] = useState<string|undefined>(undefined);
  const [selectedTag, setSelectedTag] = useState<string | undefined>(undefined);
  const [searchQuery, setSearchQuery] = useState<string>('');

  const [debouncedSearch, setDebouncedSearch] = useState<string>('');

  useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedSearch(searchQuery);
    }, 300);
    return () => clearTimeout(timer);
  }, [searchQuery]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        if (!posts) setLoading(true);
        const [postsResponse, categoriesResponse, tagsResponse] = await Promise.all([
          apiService.getPosts({      
            categoryId: selectedCategory != undefined ? selectedCategory : undefined,
            tagId: selectedTag || undefined
          }),
          apiService.getCategories(),
          apiService.getTags()
        ]);

        setPosts(postsResponse);
        setCategories(categoriesResponse);
        setTags(tagsResponse);
        setError(null);
      } catch (err) {
        setError('Failed to load content. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [page, sortBy, selectedCategory, selectedTag]);

  // Filter posts based on search query
  const filteredPosts = posts?.filter(post => {
    if (!debouncedSearch.trim()) return true;
    const query = debouncedSearch.toLowerCase();
    return (
      post.title.toLowerCase().includes(query) ||
      post.content.toLowerCase().includes(query)
    );
  }) || null;

  return (
    <div className="max-w-6xl mx-auto px-4 space-y-6">
      <HeroSection />

      <Card className="bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700 rounded-lg">
        <CardBody className="p-6">
          <div className="space-y-6">
            {/* Category Filter Row */}
            <div className="flex items-center gap-3 flex-wrap">
              <span className="text-sm font-semibold whitespace-nowrap text-gray-800 dark:text-gray-100">Category:</span>
              <div className="flex gap-2 flex-wrap">
                <button
                  onClick={() => setSelectedCategory(undefined)}
                  className={`px-4 py-1.5 rounded-full text-sm font-medium transition-colors ${
                    selectedCategory === undefined
                      ? 'bg-primary text-white'
                      : 'bg-gray-200 text-gray-800 hover:bg-gray-300 dark:bg-gray-600 dark:text-gray-100 dark:hover:bg-gray-500'
                  }`}
                >
                  All
                </button>
                {categories.map((category) => (
                  <button
                    key={category.id}
                    onClick={() => setSelectedCategory(category.id)}
                    className={`px-4 py-1.5 rounded-full text-sm font-medium transition-colors ${
                      selectedCategory === category.id
                        ? 'bg-primary text-white'
                        : 'bg-gray-200 text-gray-800 hover:bg-gray-300 dark:bg-gray-600 dark:text-gray-100 dark:hover:bg-gray-500'
                    }`}
                  >
                    {category.name}
                  </button>
                ))}
              </div>
            </div>

            {/* Tag Filter Row */}
            {tags.length > 0 && (
              <div className="flex items-center gap-3 flex-wrap">
                <span className="text-sm font-semibold whitespace-nowrap text-gray-800 dark:text-gray-100">Tags:</span>
                <div className="flex gap-2 flex-wrap">
                  <button
                    onClick={() => setSelectedTag(undefined)}
                    className={`px-4 py-1.5 rounded-full text-sm font-medium transition-colors ${
                      selectedTag === undefined
                        ? 'bg-primary text-white'
                        : 'bg-gray-200 text-gray-800 hover:bg-gray-300 dark:bg-gray-600 dark:text-gray-100 dark:hover:bg-gray-500'
                    }`}
                  >
                    All
                  </button>
                  {tags.map((tag) => (
                    <button
                      key={tag.id}
                      onClick={() => setSelectedTag(selectedTag == tag.id ? undefined : tag.id)}
                      className={`px-4 py-1.5 rounded-full text-sm font-medium transition-colors ${
                        selectedTag === tag.id
                          ? 'bg-primary text-white'
                          : 'bg-gray-200 text-gray-800 hover:bg-gray-300 dark:bg-gray-600 dark:text-gray-100 dark:hover:bg-gray-500'
                      }`}
                    >
                      {tag.name}
                    </button>
                  ))}
                </div>
              </div>
            )}

            {/* Search Bar */}
            <div className = "px-1 mt-2">
              <Input
                placeholder="Search posts..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="bg-gray-100 dark:bg-gray-900 text-gray-900 dark:text-white placeholder-gray-400 rounded-lg"
                contentLeft={<Search className="text-gray-400" />}
              />
            </div>
          </div>

          <div className="mt-4">
              <PostList
                posts={filteredPosts}
                loading={loading}
                error={error}
                page={page}
                sortBy={sortBy}
                onPageChange={setPage}
                onSortChange={setSortBy}
              />
            </div>
        </CardBody>
      </Card>
    </div>
  );
};

export default HomePage;